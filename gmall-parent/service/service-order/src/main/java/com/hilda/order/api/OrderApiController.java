package com.hilda.order.api;

import com.hilda.common.result.Result;
import com.hilda.model.vo.order.OrderConfirmPageVo;
import com.hilda.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/inner")
public class OrderApiController {

    @Autowired
    private OrderBizService orderBizService;

    @GetMapping("/getOrderConfirmPageData")
    public Result<OrderConfirmPageVo> getOrderConfirmPageData() {
        OrderConfirmPageVo orderConfirmPageData = orderBizService.getOrderConfirmPageData();

        return Result.ok(orderConfirmPageData);
    }

}
