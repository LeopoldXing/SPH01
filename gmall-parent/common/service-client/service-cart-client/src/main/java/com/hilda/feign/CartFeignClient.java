package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("service-cart")
@RequestMapping("/api/cart/inner")
public interface CartFeignClient {

    @GetMapping("/add/{skuId}")
    Result<Map<String, Object>> addCartItem(@PathVariable("skuId") Long skuId, @RequestParam Integer skuNum);

    @GetMapping("/getCartItem")
    Result<List<CartInfo>> getCartItem();

}
