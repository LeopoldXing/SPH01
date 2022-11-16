package com.hilda.product.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${minio.endpointUrl}")
    private String endpointUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Test
    void trademarkPicUpload() {
        System.out.println(endpointUrl);
        System.out.println(accessKey);
        System.out.println(secretKey);
    }

    @Test
    void redisTest() {
        System.out.println("=======================================");
        System.out.println(stringRedisTemplate);
        System.out.println("=======================================");
        stringRedisTemplate.opsForValue().set("1", "1");
        System.out.println(stringRedisTemplate.opsForValue().get("1"));
    }
}