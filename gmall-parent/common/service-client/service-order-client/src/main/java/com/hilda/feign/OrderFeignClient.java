package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.vo.order.OrderConfirmPageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-order")
@RequestMapping("/api/order/inner")
public interface OrderFeignClient {

    @GetMapping("/getOrderConfirmPageData")
    Result<OrderConfirmPageVo> getOrderConfirmPageData();

}
