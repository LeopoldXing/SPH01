package com.hilda.search.service.impl;

import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
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
        searchParamVo = new SearchParamVo();
        searchParamVo.setCategory3Id(61L);
        searchParamVo.setTrademark("2:华为");
        searchParamVo.setProps(new String[]{"4:256GB:机身存储", "3:8GB:运行内存"});
        searchParamVo.setPageNo(1);

        //TODO 构建 DSL
        Query query = this.buildQueryDSL(searchParamVo);

        //TODO 搜索
        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        //TODO 根据搜索结果构建返回对象
        SearchResponseVo res = this.hits2ResponseVo(goods, searchParamVo);

        return res;
    }

}
