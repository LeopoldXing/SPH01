package com.hilda.product.service.impl;

import com.hilda.product.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private FileService fileService;

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
}