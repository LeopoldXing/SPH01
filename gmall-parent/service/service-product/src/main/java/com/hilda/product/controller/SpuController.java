package com.hilda.product.controller;

import com.hilda.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("SPU接口")
@RestController
@RequestMapping("/admin/product")
public class SpuController {

    @ApiOperation("获取SPU分页列表")
    @GetMapping("/{page}/{limit}")
    public Result getSPU_ListByConditionsInPages(@PathVariable("page") Integer current,
                                                 @PathVariable("limit") Integer size,
                                                 Long category3Id) {


        return Result.ok();
    }

}
