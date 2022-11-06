package com.hilda.item.controller;

import com.hilda.common.result.Result;
import com.hilda.item.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api("商品服务接口")
@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ApiOperation("根据 SkuId 获取 SKU详情信息")
    @GetMapping("/{skuId}")
    public Result<Map<String, Object>> getSkuInfoById(@PathVariable Long skuId) {
        Map<String, Object> skuInfo = itemService.getSkuDetailById(skuId);
        return skuInfo == null ? Result.fail() : Result.ok(skuInfo);
    }

}
