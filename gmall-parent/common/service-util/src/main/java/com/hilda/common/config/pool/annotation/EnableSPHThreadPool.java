package com.hilda.common.config.pool.annotation;

import com.hilda.common.config.pool.SPHThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SPHThreadPoolAutoConfiguration.class)
public @interface EnableSPHThreadPool {
}
