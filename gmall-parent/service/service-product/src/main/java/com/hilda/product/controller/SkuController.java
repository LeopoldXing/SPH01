package com.hilda.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.common.result.Result;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.product.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("SKU接口")
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @ApiOperation("添加SKU")
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        return skuService.addSkuInfo(skuInfo) ? Result.ok() : Result.fail();
    }

    @ApiOperation("分页获取 SKU 列表")
    @GetMapping("/list/{page}/{limit}")
    public Result<Page<SkuInfo>> getSkuInfoInPages(@PathVariable("page") Integer current, @PathVariable("limit") Integer size) {
        Page<SkuInfo> skuInfoPage = skuService.getSkuInfoInPages(current, size);
        return skuInfoPage == null ? Result.fail() : Result.ok(skuInfoPage);
    }

    @ApiOperation("商品上架")
    @GetMapping("/onSale/{skuId}")
    public Result skuOnSale(@PathVariable Long skuId) {
        return skuService.onSale(skuId) ? Result.ok() : Result.fail();
    }

    @ApiOperation("商品下架")
    @GetMapping("/cancelSale/{skuId}")
    public Result skuOffSale(@PathVariable Long skuId) {
        return skuService.offSale(skuId) ? Result.ok() : Result.fail();
    }

}
