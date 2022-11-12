package com.hilda.search.api;

import com.hilda.common.result.Result;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import com.hilda.search.service.SearchGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/search/inner")
public class SearchApiController {

    @Autowired
    private SearchGoodService searchGoodService;

    @PostMapping("/goods")
    public Result<SearchResponseVo> searchGoodsByConditions(@RequestBody SearchParamVo searchParamVo) {

        SearchResponseVo searchResponseVo = searchGoodService.search(searchParamVo);

        return Result.ok(searchResponseVo);
    }

}
