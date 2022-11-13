package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-search")
public interface SearchFeignClient {

    @PostMapping("/api/search/inner/goods")
    Result<SearchResponseVo> searchGoodsByConditions(@RequestBody SearchParamVo searchParamVo);

    @PostMapping("/api/search/inner/goods/onSale")
    Result goodsOnSale(@RequestBody Goods goods);

    @GetMapping("/api/search/inner/goods/offSale/{id}")
    Result goodsOffSale(@PathVariable("id") Long goodsId);

}
