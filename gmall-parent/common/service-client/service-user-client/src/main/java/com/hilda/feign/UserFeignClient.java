package com.hilda.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("service-user")
public interface UserFeignClient {

}
