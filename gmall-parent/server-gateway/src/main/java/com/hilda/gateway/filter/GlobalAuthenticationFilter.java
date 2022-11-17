package com.hilda.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.hilda.common.result.Result;
import com.hilda.common.result.ResultCodeEnum;
import com.hilda.gateway.config.AuthUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GlobalAuthenticationFilter implements GlobalFilter {

    @Autowired
    private AuthUrlProperties authUrlProperties;

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

        Mono<Void> filterMono = chain.filter(exchange);

        return filterMono;
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

}
