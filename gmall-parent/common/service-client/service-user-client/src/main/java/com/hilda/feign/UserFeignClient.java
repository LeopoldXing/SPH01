package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.bean.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-user")
@RequestMapping("/api/user/inner")
public interface UserFeignClient {

    @GetMapping("/address/list")
    Result<List<UserAddress>> getUserAddress();

}
