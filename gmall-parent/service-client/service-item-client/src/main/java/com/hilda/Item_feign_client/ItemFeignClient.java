package com.hilda.Item_feign_client;

import com.hilda.model.vo.item.SkuDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-item")
public interface ItemFeignClient {

    @GetMapping("/api/item/inner/getSkuDetail/{skuId}")
    SkuDetailVo getSkuDetailBySkuId(@PathVariable("skuId") Long skuId);

}
