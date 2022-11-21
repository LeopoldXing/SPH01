package com.hilda.weball.interceptor;

import com.hilda.common.constant.RedisConst;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserFeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String uid = "";
        String tempUID = "";

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            if (!ObjectUtils.isEmpty(request)) {
                uid = request.getHeader(RedisConst.UID);
                tempUID = request.getHeader(RedisConst.TEMP_UID);
            }
        }

        requestTemplate.header(RedisConst.UID, uid);
        requestTemplate.header(RedisConst.TEMP_UID, tempUID);
    }

}
