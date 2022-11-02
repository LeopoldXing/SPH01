package com.hilda.common.config.swagger.annotation;

import com.hilda.common.config.swagger.Swagger2AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(Swagger2AutoConfiguration.class)
public @interface EnableSwagger {
}
