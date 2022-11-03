package com.hilda.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.common.result.Result;
import com.hilda.model.bean.product.BaseSaleAttr;
import com.hilda.model.bean.product.SpuImage;
import com.hilda.model.bean.product.SpuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.product.SpuInfoVo;
import com.hilda.product.service.SpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("SPU接口")
@RestController
@RequestMapping("/admin/product")
public class SpuController {

    @Autowired
    private SpuService spuService;

    @ApiOperation("分页获取SPU列表")
    @GetMapping("/{page}/{limit}")
    public Result<Page<SpuInfo>> getSPUInfoListByCategory3IdInPages(@PathVariable("page") Integer current,
                                                                    @PathVariable("limit") Integer size,
                                                                    Long category3Id) {
        Page<SpuInfo> page = spuService.getSPUInfoListByCategory3IdInPages(current, size, category3Id);

        return Result.ok(page);
    }

    @ApiOperation("获取销售属性")
    @GetMapping("/baseSaleAttrList")
    public Result<List<BaseSaleAttr>> getBaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = spuService.getBaseSaleAttrList();
        return baseSaleAttrList == null ? Result.fail() : Result.ok(baseSaleAttrList);
    }

    @ApiOperation("添加SPU")
    @PostMapping("/saveSpuInfo")
    public Result addSpu(@RequestBody SpuInfoVo spuInfoVo) {
        return spuService.addSpu(spuInfoVo) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据SpuId获取图片列表")
    @GetMapping("/spuImageList/{spuId}")
    public Result<List<SpuImage>> getImageListBySpuId(@PathVariable Long spuId) {
        List<SpuImage> spuImageList = spuService.getImageListBySpuId(spuId);
        return spuImageList == null ? Result.fail() : Result.ok(spuImageList);
    }

    @ApiOperation("根据SpuId获取销售属性")
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListBySpuId(@PathVariable Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuService.getSpuSaleAttrListBySpuId(spuId);
        return spuSaleAttrList == null ? Result.fail() : Result.ok(spuSaleAttrList);
    }

}
