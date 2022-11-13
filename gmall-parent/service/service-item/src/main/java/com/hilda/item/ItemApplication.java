package com.hilda.item;

import com.hilda.common.config.swagger.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableSwagger
@EnableFeignClients(basePackages = "com.hilda.feign")
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }

}
