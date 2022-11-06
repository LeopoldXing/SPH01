package com.hilda.Item_feign_client;

import com.hilda.model.vo.product.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "service-item")
public interface ItemFeignClient {

}
