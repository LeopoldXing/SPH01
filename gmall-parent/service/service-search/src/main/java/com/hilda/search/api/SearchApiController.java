package com.hilda.search.api;

import com.hilda.common.result.Result;
import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import com.hilda.search.service.GoodService;
import com.hilda.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/inner")
public class SearchApiController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodService goodService;

    @PostMapping("/goods")
    public Result<SearchResponseVo> searchGoodsByConditions(@RequestBody SearchParamVo searchParamVo) {
        SearchResponseVo searchResponseVo = searchService.searchGoodsByConditions(searchParamVo);

        return Result.ok(searchResponseVo);
    }

    @PostMapping("/goods/onSale")
    public Result goodsOnSale(@RequestBody Goods goods) {
        return goodService.onSale(goods) ? Result.ok(): Result.fail();
    }

    @GetMapping("/goods/offSale/{id}")
    public Result goodsOffSale(@PathVariable("id") Long goodsId) {
        return goodService.offSale(goodsId) ? Result.ok() : Result.fail();
    }

}
