package com.hilda.common.util;

import com.hilda.common.constant.RedisConst;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtil {

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            request = attributes.getRequest();
        }
        return request;
    }

    public static Long getUID() {
        Long uid = 0L;
        HttpServletRequest request = RequestUtil.getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            uid = Long.parseLong(request.getHeader(RedisConst.UID));
        }

        return uid;
    }

    public static String getTempUID() {
        String tempUID = "";
        HttpServletRequest request = RequestUtil.getRequest();
        if (!ObjectUtils.isEmpty(request)) {
            tempUID = request.getHeader(RedisConst.TEMP_UID);
        }

        return tempUID;
    }

    public static Boolean isTemp() {
        Long uid = getUID();
        String tempUID = getTempUID();

        Boolean isTemp = true;
        if (uid != null && uid != 0) isTemp = false;

        return isTemp;
    }

}
