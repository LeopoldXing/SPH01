package com.hilda.item.api;

import com.hilda.item.service.ItemService;
import com.hilda.model.vo.item.SkuDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("商品 RPC 接口")
@RestController
@RequestMapping("/api/item/inner")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    @ApiOperation("根据 SkuId 查询商品详情")
    @GetMapping("/getSkuDetail/{skuId}")
    public SkuDetailVo getItemDetailBySkuId(@PathVariable("skuId") Long skuId) {
        SkuDetailVo skuDetailVo = itemService.getSkuDetailBySkuId(skuId);

        return skuDetailVo;
    }

}
