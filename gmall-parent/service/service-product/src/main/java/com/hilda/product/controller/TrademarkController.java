package com.hilda.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hilda.common.result.Result;
import com.hilda.model.product.BaseTrademark;
import com.hilda.product.service.TrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("商标接口")
@RestController
@RequestMapping("/admin/product")
public class TrademarkController {

    @Autowired
    private TrademarkService trademarkService;

    @ApiOperation("根据id查询品牌详情")
    @GetMapping("/baseTrademark/get/{id}")
    public Result<BaseTrademark> getTrademarkById(@PathVariable Long id) {
        BaseTrademark baseTrademark = trademarkService.getTrademarkById(id);
        return baseTrademark == null ? Result.fail() : Result.ok(baseTrademark);
    }

    @ApiOperation("分页获取品牌列表")
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result<IPage<BaseTrademark>> getTrademarkInPages(@PathVariable("page") Integer current, @PathVariable("limit") Integer size) {
        IPage<BaseTrademark> currentPage = trademarkService.getTrademarkInPages(current, size);

        return currentPage == null ? Result.fail() : Result.ok(currentPage);
    }

    @ApiOperation("添加品牌")
    @PostMapping("/baseTrademark/save")
    public Result addTrademark(@RequestBody BaseTrademark baseTrademark) {
        return trademarkService.addTrademark(baseTrademark) ? Result.ok() : Result.fail();
    }

    @ApiOperation("修改品牌")
    @PutMapping("/baseTrademark/update")
    public Result updateTrademark(@RequestBody BaseTrademark baseTrademark) {
        return trademarkService.updateTrademark(baseTrademark) ? Result.ok() : Result.fail();
    }

    @ApiOperation("删除品牌")
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result deleteTrademarkById(@PathVariable Long id) {
        return trademarkService.deleteTrademarkById(id) ? Result.ok() : Result.fail();
    }

}
