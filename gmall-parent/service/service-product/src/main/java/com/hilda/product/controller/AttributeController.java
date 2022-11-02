package com.hilda.product.controller;

import com.hilda.common.result.Result;
import com.hilda.model.bean.product.BaseAttrInfo;
import com.hilda.model.bean.product.BaseAttrValue;
import com.hilda.product.service.AttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("属性相关接口")
@RestController
@RequestMapping("/admin/product")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @ApiOperation("根据分类Id 获取平台属性数据")
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id") Long category1Id,
                                                   @PathVariable("category2Id") Long category2Id,
                                                   @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> attrInfoList = attributeService.getAttrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(attrInfoList);
    }

    @ApiOperation("添加或修改平台属性")
    @PostMapping("/saveAttrInfo")
    public Result saveBaseAttrInfo (@RequestBody BaseAttrInfo baseAttrInfo) {
        return attributeService.saveBaseAttrInfo(baseAttrInfo) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据平台属性id获取平台属性信息")
    @GetMapping("/getAttrValueList/{attrId}")
    public Result<List<BaseAttrValue>> getBaseAttrInfoByAttrId (@PathVariable Long attrId) {
        BaseAttrInfo baseAttrInfo = attributeService.getAttrInfoById(attrId);
        return (baseAttrInfo == null || baseAttrInfo.getAttrValueList() == null)
                ? Result.fail()
                : Result.ok(baseAttrInfo.getAttrValueList());
    }

}
