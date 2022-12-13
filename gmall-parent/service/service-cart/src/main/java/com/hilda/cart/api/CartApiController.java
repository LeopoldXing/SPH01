package com.hilda.cart.api;

import com.hilda.cart.service.CartService;
import com.hilda.common.result.Result;
import com.hilda.feign.ProductFeignClient;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart/inner")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductFeignClient productFeignClient;

    @GetMapping("/add/{skuId}")
    public Result<Map<String, Object>> addCartItem(@PathVariable("skuId") Long skuId, @RequestParam Integer skuNum) {
        Boolean addResult = cartService.addCartItem(skuId, skuNum);

        if(addResult) {
            Map<String, Object> res = new HashMap<>();
            res.put("skuInfo", productFeignClient.getSkuInfoById(skuId));
            res.put("skuNum", skuNum);
            return Result.ok(res);
        }
        return Result.fail();
    }

    @DeleteMapping("/deleteChecked")
    public Result deleteCheckedItem() {
        Boolean res = cartService.deleteCheckedItems();

        return res ? Result.ok() : Result.fail();
    }

    @GetMapping("/cartInfo/list")
    public Result<List<CartInfo>> getCheckedItemInfoList(){
        List<CartInfo> cartItemList = cartService.getCartItemList();

        return Result.ok(cartItemList);
    }

}
