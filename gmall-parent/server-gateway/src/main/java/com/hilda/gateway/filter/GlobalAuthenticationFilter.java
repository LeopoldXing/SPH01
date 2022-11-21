package com.hilda.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hilda.common.constant.RedisConst;
import com.hilda.common.execption.GmallException;
import com.hilda.common.result.Result;
import com.hilda.common.result.ResultCodeEnum;
import com.hilda.common.util.IpUtil;
import com.hilda.gateway.config.AuthUrlProperties;
import com.hilda.model.bean.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GlobalAuthenticationFilter implements GlobalFilter {

    @Autowired
    private AuthUrlProperties authUrlProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. 获取请求的路径
        String path = request.getURI().getPath();

        // 2. 判断路径
        // 2.1 静态资源
        List<String> directPassUrlList = authUrlProperties.getDirectPassUrl();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        long staticCount = directPassUrlList
                .parallelStream()
                .filter(pattern -> antPathMatcher.match(pattern, path))
                .count();
        if (staticCount > 0) {
            System.out.println("静态资源放行");
            return chain.filter(exchange);
        }
        // 2.2 内部调用请求
        List<String> innerUrl = authUrlProperties.getInnerUrl();
        long innerCount = innerUrl.parallelStream().filter(pattern -> antPathMatcher.match(pattern, path)).count();
        if (innerCount > 0) {
            System.out.println("拦截尝试访问内部调用接口的外部请求");
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }
        // 2.3 判断需要登录的请求
        List<String> requiredAuthUrl = authUrlProperties.getRequiredAuthUrl();
        long loginUrlCount = requiredAuthUrl.parallelStream().filter(pattern -> antPathMatcher.match(pattern, path)).count();
        if (loginUrlCount > 0) {
            // 判断登录状态
            String token = this.getTokenFromRequest(exchange);
            if (StringUtils.isEmpty(token) || !this.tokenExistInRedis(token)) {
                // 未登录 或 token无效，重定向到登录页
                return redirectPage(exchange, authUrlProperties.getLoginPage());
            }
        }

        // 3. 处理正常请求
        UserInfo userInfo = null;
        // 3.1 对带了token的正常请求进行验证
        String token = this.getTokenFromRequest(exchange);
        if (!StringUtils.isEmpty(token) && !this.tokenExistInRedis(token)) {
            // token不为空且无效
            return redirectPage(exchange, authUrlProperties.getLoginPage());
        } else if (!StringUtils.isEmpty(token)) {
            // token不为空且有效
            userInfo = getUserInfoByToken(token);
            if (!ObjectUtils.isEmpty(userInfo)) {
                // 获取当前ip 和 登录时的ip
                String loginIp = userInfo.getLoginIp();
                String currentIp = IpUtil.getGatwayIpAddress(request);
                if (!StringUtils.isEmpty(loginIp)) {
                    if (!loginIp.equalsIgnoreCase(currentIp)) {
                        // 令牌有效，ip不同
                        return redirectPage(exchange, authUrlProperties.getLoginPage());
                    }
                } else {
                    // 登录ip地址为空
                    return redirectPage(exchange, authUrlProperties.getLoginPage());
                }
            } else {
                // 根据令牌查不到用户信息
                return redirectPage(exchange, authUrlProperties.getLoginPage());
            }
        }

        return userIdPenetration(exchange, chain, userInfo);
    }

    /**
     * 用户id穿透
     * @param exchange
     * @param chain
     * @param userInfo
     * @return
     */
    private Mono<Void> userIdPenetration(ServerWebExchange exchange, GatewayFilterChain chain, UserInfo userInfo) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 如果用户信息为空则查询临时id后放行
        String tempUID = "";
        if (ObjectUtils.isEmpty(userInfo)) {
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            if (!ObjectUtils.isEmpty(cookies)) {
                HttpCookie tempUIDCookie = cookies.getFirst(RedisConst.TEMP_UID);
                if (!ObjectUtils.isEmpty(tempUIDCookie)) tempUID = tempUIDCookie.getValue();
            }
            if (!StringUtils.isEmpty(tempUID)) {
                ServerHttpRequest tempNewRequest = request.mutate().header(RedisConst.TEMP_UID, tempUID).build();
                ServerWebExchange newExchange = exchange.mutate().request(tempNewRequest).response(response).build();

                return chain.filter(newExchange);
            }
            return chain.filter(exchange);
        }

        // 用户id透传
        ServerHttpRequest newRequest = request.mutate().header(RedisConst.UID, String.valueOf(userInfo.getId())).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).response(response).build();

        return chain.filter(newExchange);
    }

    private Mono<Void> redirectPage(ServerWebExchange exchange, String uri) {
        if (StringUtils.isEmpty(uri)) {
            throw new GmallException("重定向地址为空", 100);
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        String path = "";
        ServerHttpRequest request = exchange.getRequest();
        try {
            path = request.getURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String url = uri + "?originUrl=" + path;
        response.getHeaders().add("Location", url);
        if (uri.equalsIgnoreCase(authUrlProperties.getLoginPage())) {
            // 如果跳转到登录页则
            ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                    .path("/")
                    .domain("gmall.com")
                    .maxAge(0)
                    .build();
            ResponseCookie userInfoCookie = ResponseCookie.from("userInfo", "")
                    .path("/")
                    .domain("gmall.com")
                    .maxAge(0)
                    .build();
            response.addCookie(tokenCookie);
            response.addCookie(userInfoCookie);
        }
        return response.setComplete();
    }

    // 接口鉴权失败返回数据
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        // 返回用户没有权限登录
        Result<Object> result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bits);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        // 输入到页面
        return response.writeWith(Mono.just(wrap));
    }

    // 拿到请求中的token
    private String getTokenFromRequest(ServerWebExchange exchange) {
        String res = "";
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (!ObjectUtils.isEmpty(cookies)) {
            HttpCookie tokenCookie = cookies.getFirst("token");
            if (!ObjectUtils.isEmpty(tokenCookie)) res = tokenCookie.getValue();
            if (!StringUtils.isEmpty(res)) {
                return res;
            }
        }

        return request.getHeaders().getFirst("token");
    }

    private boolean tokenExistInRedis(String token) {
        String key = RedisConst.USERKEY_PREFIX + RedisConst.USERKEY_SUFFIX + token;
        Boolean tokenExist = redisTemplate.hasKey(key);
        return tokenExist;
    }

    private UserInfo getUserInfoByToken(String token) {
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(token)) {
            String key = RedisConst.USERKEY_PREFIX + RedisConst.USERKEY_SUFFIX + token;
            String userInfoJson = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(userInfoJson)) {
                userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
            }
        }
        return userInfo;
    }

}
