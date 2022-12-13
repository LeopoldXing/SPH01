package com.hilda.order;

import com.hilda.common.config.feign.annotation.EnableUserAuthentication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableUserAuthentication
@EnableFeignClients("com.hilda.feign")
@MapperScan("com.hilda.order.mapper")
@SpringCloudApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
