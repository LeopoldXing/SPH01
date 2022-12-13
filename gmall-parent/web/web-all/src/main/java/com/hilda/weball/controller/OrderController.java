package com.hilda.weball.controller;

import com.hilda.common.result.Result;
import com.hilda.feign.OrderFeignClient;
import com.hilda.model.vo.order.OrderConfirmPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/trade.html")
    public ModelAndView OrderConfirmPageRender() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order/trade");

        OrderConfirmPageVo res = new OrderConfirmPageVo();

        Result<OrderConfirmPageVo> orderConfirmPageDataResult = orderFeignClient.getOrderConfirmPageData();
        if (!ObjectUtils.isEmpty(orderConfirmPageDataResult)) res = orderConfirmPageDataResult.getData();

        modelAndView.addObject("detailArrayList", res.getDetailArrayList());
        modelAndView.addObject("totalNum", res.getTotalNum());
        modelAndView.addObject("totalAmount", res.getTotalAmount());
        modelAndView.addObject("userAddressList", res.getUserAddressList());
        modelAndView.addObject("tradeNo", res.getTradeNo());

        return modelAndView;
    }

}
