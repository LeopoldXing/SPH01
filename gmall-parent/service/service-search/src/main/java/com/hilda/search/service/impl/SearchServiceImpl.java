package com.hilda.search.service.impl;

import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import com.hilda.search.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public SearchResponseVo searchGoodsByConditions(SearchParamVo searchParamVo) {
        SearchResponseVo res = new SearchResponseVo();

        return res;
    }

}
