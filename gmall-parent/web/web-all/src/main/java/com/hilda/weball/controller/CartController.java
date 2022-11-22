package com.hilda.weball.controller;

import com.hilda.common.result.Result;
import com.hilda.feign.CartFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;

    @GetMapping("/cart.html")
    public ModelAndView renderCartPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart/index");

        return modelAndView;
    }

    @GetMapping("/addCart.html")
    public ModelAndView renderAddCartPage(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart/addCart");

        Result<Map<String, Object>> result = cartFeignClient.addCartItem(skuId, skuNum);

        if (!ObjectUtils.isEmpty(result)) {
            Map<String, Object> map = result.getData();
            if (!ObjectUtils.isEmpty(map)) {
                modelAndView.addObject("skuInfo", map.get("skuInfo"));
                modelAndView.addObject("skuNum", map.get("skuNum"));
            }
        }

        return modelAndView;
    }

    @GetMapping("/cart/deleteChecked")
    public ModelAndView deleteCartItem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:http://cart.gmall.com/cart.html");

        cartFeignClient.deleteCheckedItem();

        return modelAndView;
    }

}
