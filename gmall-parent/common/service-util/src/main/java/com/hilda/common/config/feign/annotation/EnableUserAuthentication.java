package com.hilda.common.config.feign.annotation;

import com.hilda.common.config.feign.interceptor.UserFeignHeaderInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserFeignHeaderInterceptor.class)
public @interface EnableUserAuthentication {
}
