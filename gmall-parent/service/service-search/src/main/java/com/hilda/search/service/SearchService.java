package com.hilda.search.service;

import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.list.SearchParamVo;
import com.hilda.model.vo.list.SearchResponseVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public interface SearchService {

    SearchResponseVo searchGoodsByConditions(SearchParamVo searchParamVo);

    default Query buildQueryDSL(SearchParamVo searchParamVo) {
        // 从 SearchParamVo中取出数据
        Long category1Id = searchParamVo.getCategory1Id();
        Long category2Id = searchParamVo.getCategory2Id();
        Long category3Id = searchParamVo.getCategory3Id();
        String keyword = searchParamVo.getKeyword();
        Integer pageNo = searchParamVo.getPageNo();
        String order = searchParamVo.getOrder();
        String[] props = searchParamVo.getProps();
        String trademark = searchParamVo.getTrademark();

        //TODO 1. 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 1. 分类条件查询
        if (category1Id != null)
            boolQuery.must(QueryBuilders.termQuery("category1Id", category1Id));

        if (category2Id != null)
            boolQuery.must(QueryBuilders.termQuery("category2Id", category2Id));

        if (category3Id != null)
            boolQuery.must(QueryBuilders.termQuery("category3Id", category3Id));

        // 2. 商品名查询
        if (!StringUtils.isEmpty(keyword)) boolQuery.must(QueryBuilders.matchQuery("title", keyword));

        // 3. 品牌查询
        if (!StringUtils.isEmpty(trademark)) {
            Long tmId = 0L;
            String[] split = trademark.split(":");
            if (!ObjectUtils.isEmpty(split)) {
                tmId = Long.parseLong(split[0]);
                boolQuery.must(QueryBuilders.termQuery("tmId", tmId));
            }
        }

        // 4. 属性查询
        if (!ObjectUtils.isEmpty(props)) {
            Arrays.stream(props).parallel().forEach(properties -> {
                if (!StringUtils.isEmpty(properties)) {
                    String[] split = properties.split(":");
                    Long attrId = Long.parseLong(split[0]);
                    String attrValue = split[1];
                    BoolQueryBuilder query = QueryBuilders.boolQuery();
                    query.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                    query.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));
                    boolQuery.must(QueryBuilders.nestedQuery("attrs", query, ScoreMode.None));
                }
            });
        }

        //TODO 2. 构建DSL
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQuery);

        //TODO 3. 排序
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            if (!ObjectUtils.isEmpty(split)) {
                String type = split[0];
                String directionNative = split[1];
                String field = "hotScore";
                switch (type) {
                    case "1":
                        field = "price";
                        break;
                    case "2":
                        field = "hotScore";
                }

                String direction = "DESC";
                if (!StringUtils.isEmpty(directionNative) && directionNative.equalsIgnoreCase("ASC"))
                    direction = "ASC";

                Sort sort = Sort.by(Sort.Direction.fromString(direction), field);
                nativeSearchQuery.addSort(sort);
            }
        }

        //TODO 4. 分页
        if (pageNo != null && pageNo > 0L) {
            Pageable pageable = PageRequest.of(pageNo - 1, 10);
            nativeSearchQuery.setPageable(pageable);
        }

        return nativeSearchQuery;
    }

    default SearchResponseVo hits2ResponseVo(SearchHits<Goods> searchResult, SearchParamVo searchParamVo) {
        SearchResponseVo res = new SearchResponseVo();
        res.setSearchParamVo(searchParamVo);

        SearchHit<Goods> searchHitList = searchResult.getSearchHit();
    }

}
