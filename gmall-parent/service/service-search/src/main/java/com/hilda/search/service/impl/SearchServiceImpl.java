package com.hilda.search.service.impl;

import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.search.SearchParamVo;
import com.hilda.model.vo.search.SearchResponseVo;
import com.hilda.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    @Override
    public SearchResponseVo searchGoodsByConditions(SearchParamVo searchParamVo) {

        //TODO 构建 DSL
        Query query = this.buildQueryDSL(searchParamVo);

        //TODO 搜索
        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        //TODO 根据搜索结果构建返回对象
        SearchResponseVo res = this.hits2ResponseVo(goods, searchParamVo);

        return res;
    }

}
