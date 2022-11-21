package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-cart")
@RequestMapping("/api/cart/inner")
public interface CartFeignClient {

    @GetMapping("/getCartItem")
    Result<List<CartInfo>> getCartItem();

}
