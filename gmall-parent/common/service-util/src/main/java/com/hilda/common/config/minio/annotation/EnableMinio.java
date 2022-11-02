package com.hilda.common.config.minio.annotation;

import com.hilda.common.config.minio.MinioAutoConfiguration;
import com.hilda.common.config.minio.properties.MinioProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MinioAutoConfiguration.class)
@EnableConfigurationProperties(MinioProperties.class)
public @interface EnableMinio {
}
