package com.hilda.weball.controller;

import com.hilda.common.constant.RedisConst;
import com.hilda.common.result.Result;
import com.hilda.feign.CartFeignClient;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;

    @GetMapping("/cart.html")
    public ModelAndView renderCartPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart/index");

        String uid = request.getHeader(RedisConst.UID);
        String tempUID = request.getHeader(RedisConst.TEMP_UID);

        Result<List<CartInfo>> result = cartFeignClient.getCartItem();
        if (!ObjectUtils.isEmpty(result)) {
            List<CartInfo> cartItemList = result.getData();
            System.out.println(cartItemList);
        }

        return modelAndView;
    }

}
