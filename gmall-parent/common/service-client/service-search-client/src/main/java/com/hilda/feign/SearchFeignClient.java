package com.hilda.feign;

import com.hilda.common.result.Result;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-search")
public interface SearchFeignClient {

    @PostMapping("/goods")
    Result<SearchResponseVo> searchGoodsByConditions(@RequestBody SearchParamVo searchParamVo);

}
