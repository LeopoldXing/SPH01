package com.hilda.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
// @SpringBootApplication
// @EnableDiscoveryClient
// @EnableCircuitBreaker       // 开启服务熔断功能
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
