package com.hilda.cart.controller;

import com.hilda.cart.service.CartService;
import com.hilda.common.result.Result;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cartList")
    public Result getCartItemList() {
        List<CartInfo> res = cartService.getCartItemList();
        return Result.ok(res);
    }

    @PostMapping("/addToCart/{skuId}/{skuNum}")
    public Result addItemToCart(@PathVariable("skuId") Long skuId, @PathVariable("skuNum") Integer skuNum) {
        Boolean res = cartService.addCartItem(skuId, skuNum);

        return res ? Result.ok() : Result.fail();
    }

    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result checkCartItem(@PathVariable("skuId") Long skuId, @PathVariable("isChecked") Integer isChecked) {
        Boolean res = cartService.checkItem(skuId, isChecked);

        return res ? Result.ok() : Result.fail();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCartItem(@PathVariable("skuId") Long skuId) {
        cartService.deleteItem(skuId);

        return Result.ok();
    }

}
