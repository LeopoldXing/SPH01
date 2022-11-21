package com.hilda.cart.api;

import com.hilda.cart.service.CartService;
import com.hilda.common.constant.RedisConst;
import com.hilda.common.result.Result;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart/inner")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @GetMapping("/getCartItem")
    public Result<List<CartInfo>> getCartItem(HttpServletRequest request) {
        List<CartInfo> cartItemList = new ArrayList<>();
        cartItemList.add(new CartInfo());

        String tempUID = request.getHeader(RedisConst.TEMP_UID);
        String uid = request.getHeader(RedisConst.UID);

        return Result.ok(cartItemList);
    }

}
