package com.hilda.search.service;

import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;

public interface SearchGoodService {

    SearchResponseVo search(SearchParamVo searchParamVo);

}
