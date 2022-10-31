package com.hilda.product.controller;

import com.hilda.common.result.Result;
import com.hilda.model.product.BaseAttrInfo;
import com.hilda.model.product.BaseCategory1;
import com.hilda.model.product.BaseCategory2;
import com.hilda.model.product.BaseCategory3;
import com.hilda.product.service.AttributeService;
import com.hilda.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("商品基础属性接口")
@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttributeService attributeService;

    @ApiOperation("查询所有的一级分类信息")
    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1() {
        List<BaseCategory1> firstLevelCategoryList = categoryService.getFirstLevelCategoryList();
        return Result.ok(firstLevelCategoryList);
    }

    @ApiOperation("根据一级分类Id 查询二级分类数据")
    @GetMapping("/getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable("category1Id") Long category1Id){
        List<BaseCategory2> secondLevelCategoryList = categoryService.getSecondLevelCategoryList(category1Id);
        return Result.ok(secondLevelCategoryList);
    }

    @ApiOperation("根据二级分类Id 查询三级分类数据")
    @GetMapping("/getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable("category2Id") Long category2Id){
        List<BaseCategory3> thirdLevelCategoryList = categoryService.getThirdLevelCategoryList(category2Id);
        return Result.ok(thirdLevelCategoryList);
    }

    @ApiOperation("根据分类Id 获取平台属性数据")
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id") Long category1Id,
                                                   @PathVariable("category2Id") Long category2Id,
                                                   @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> attrInfoList = attributeService.getAttrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(attrInfoList);
    }

}
