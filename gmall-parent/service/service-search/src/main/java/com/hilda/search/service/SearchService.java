package com.hilda.search.service;

import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.search.SearchParamVo;
import com.hilda.model.vo.search.SearchResponseVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface SearchService {

    SearchResponseVo searchGoodsByConditions(SearchParamVo searchParamVo);

    default Query buildQueryDSL(SearchParamVo searchParamVo) {
        // 从 SearchParamVo 中取出数据
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
                    if (!ObjectUtils.isEmpty(split)) {
                        Long attrId = Long.parseLong(split[0]);
                        String attrValue = split[1];
                        BoolQueryBuilder query = QueryBuilders.boolQuery();
                        query.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                        query.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));
                        boolQuery.must(QueryBuilders.nestedQuery("attrs", query, ScoreMode.None));
                    }
                }
            });
        }

        //TODO 2. 构建DSL
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQuery);

        //TODO 3. 排序
        if (!StringUtils.isEmpty(order) && !"null".equalsIgnoreCase(order)) {
            String[] split = order.split(":");
            if (!ObjectUtils.isEmpty(split)) {
                String type = split[0];
                String directionNative = split[1];
                String field = "hotScore";
                switch (type) {
                    case "1":
                        field = "hotScore";
                        break;
                    case "2":
                        field = "price";
                }

                String direction = "DESC";
                if (!StringUtils.isEmpty(directionNative) && directionNative.equalsIgnoreCase("ASC"))
                    direction = "ASC";

                Sort sort = Sort.by(Sort.Direction.fromString(direction), field);
                nativeSearchQuery.addSort(sort);
            }
        }

        // 设置高亮
        if (!StringUtils.isEmpty(keyword)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            nativeSearchQuery.setHighlightQuery(highlightQuery);
        }

        //TODO 4. 分页
        if (pageNo == null || pageNo <= 0L) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        nativeSearchQuery.setPageable(pageable);

        //TODO 5. 聚合

        // 1. 品牌信息聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg").field("tmName").size(1);
        TermsAggregationBuilder tmLogoAgg = AggregationBuilders.terms("tmLogoAgg").field("tmLogoUrl").size(1);
        tmIdAgg.subAggregation(tmNameAgg);
        tmIdAgg.subAggregation(tmLogoAgg);
        nativeSearchQuery.addAggregation(tmIdAgg);

        // 2. 属性信息聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1);
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100);
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        NestedAggregationBuilder nestedAggregationBuilder = attrAgg.subAggregation(attrIdAgg);
        nativeSearchQuery.addAggregation(nestedAggregationBuilder);

        return nativeSearchQuery;
    }

    default SearchResponseVo hits2ResponseVo(SearchHits<Goods> searchResult, SearchParamVo searchParamVo) {
        SearchResponseVo res = new SearchResponseVo();

        String trademark = searchParamVo.getTrademark();
        String[] props = searchParamVo.getProps();
        Integer pageNo = searchParamVo.getPageNo();
        long totalHits = searchResult.getTotalHits();

        // 1. 检索参数
        res.setSearchParamVo(searchParamVo);

        // 2. 品牌面包屑
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (!ObjectUtils.isEmpty(split)) {
                String trademarkName = split[1];
                res.setTrademarkParam("品牌:" + trademarkName);
            }
        }

        // 3. 属性面包屑
        List<SearchResponseVo.AttrVo> propsParamList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(props)) {
            Arrays.stream(props).forEach(property -> {
                String[] split = property.split(":");
                if (!ObjectUtils.isEmpty(split)) {
                    SearchResponseVo.AttrVo attrVo = new SearchResponseVo.AttrVo();
                    Long attrId = Long.parseLong(split[0]);
                    String attrValue = split[1];
                    String attrName = split[2];

                    attrVo.setAttrId(attrId);
                    attrVo.setAttrName(attrName);
                    attrVo.setAttrValue(attrValue);
                    propsParamList.add(attrVo);
                }
            });
        }
        res.setPropsParamList(propsParamList);

        // 4. 加入检索到的商品
        List<Goods> goodsList = new ArrayList<>();
        for (SearchHit<Goods> hit : searchResult) {
            if (!ObjectUtils.isEmpty(hit)) {
                Goods goods = hit.getContent();
                // 设置高亮
                if (!StringUtils.isEmpty(searchParamVo.getKeyword())) {
                    String title = "";
                    List<String> titleList = hit.getHighlightField("title");
                    if (!ObjectUtils.isEmpty(titleList)) title = titleList.get(0);
                    goods.setTitle(title);
                }
                goodsList.add(goods);
            }
        }

        res.setGoodsList(goodsList);

        // 5. 商品品牌信息统计
        List<SearchResponseVo.TrademarkVo> trademarkVoList = new ArrayList<>();
        Aggregations aggregations = searchResult.getAggregations();
        if (!ObjectUtils.isEmpty(aggregations)) {
            ParsedLongTerms tmIdAgg = aggregations.get("tmIdAgg");
            List<? extends Terms.Bucket> tmIdAggBuckets = tmIdAgg.getBuckets();
            if (!ObjectUtils.isEmpty(tmIdAggBuckets)) {
                tmIdAggBuckets.parallelStream().forEach(bucket -> {
                    // 获取子聚合结果
                    Aggregations subAggregations = bucket.getAggregations();
                    SearchResponseVo.TrademarkVo trademarkVo = new SearchResponseVo.TrademarkVo();
                    // 获取品牌Id
                    long tmId = Long.parseLong(bucket.getKeyAsString());
                    // 获取品牌LogoUrl
                    String logoUrl = "";
                    ParsedStringTerms tmLogoAgg = subAggregations.get("tmLogoAgg");
                    List<? extends Terms.Bucket> tmLogoBuckets = tmLogoAgg.getBuckets();
                    if (!ObjectUtils.isEmpty(tmLogoBuckets)) {
                        Terms.Bucket tmLogoBucket = tmLogoBuckets.get(0);
                        logoUrl = tmLogoBucket.getKeyAsString();
                    }
                    // 获取品牌名称
                    String tmName = "";
                    ParsedStringTerms tmNameAgg = subAggregations.get("tmNameAgg");
                    List<? extends Terms.Bucket> tmNameAggBuckets = tmNameAgg.getBuckets();
                    if (!ObjectUtils.isEmpty(tmNameAggBuckets)) {
                        Terms.Bucket tmNameBucket = tmNameAggBuckets.get(0);
                        tmName = tmNameBucket.getKeyAsString();
                    }
                    // 封装数据
                    trademarkVo.setTmId(tmId);
                    trademarkVo.setTmName(tmName);
                    trademarkVo.setTmLogoUrl(logoUrl);
                    trademarkVoList.add(trademarkVo);
                });
            }
        }
        res.setTrademarkList(trademarkVoList);

        // 6. 商品属性信息统计
        List<SearchResponseVo.AttrListVo> attrListVoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(aggregations)) {
            ParsedNested attrAgg = aggregations.get("attrAgg");
            if (!ObjectUtils.isEmpty(attrAgg)) {
                Aggregations attrsAggregations = attrAgg.getAggregations();
                if (!ObjectUtils.isEmpty(attrsAggregations)) {
                    ParsedLongTerms attrIdAgg = attrsAggregations.get("attrIdAgg");
                    List<? extends Terms.Bucket> attrIdBuckets = attrIdAgg.getBuckets();
                    if (!ObjectUtils.isEmpty(attrIdBuckets)) {
                        attrIdBuckets.parallelStream().forEach(attrIdBucket -> {
                            // 遍历attrId聚合结果
                            SearchResponseVo.AttrListVo attrListVo = new SearchResponseVo.AttrListVo();
                            // 获取attrId
                            long attrId = Long.parseLong(attrIdBucket.getKeyAsString());

                            // 获取attrName
                            String attrName = "";
                            Aggregations attrIdSubAgg = attrIdBucket.getAggregations();
                            if (!ObjectUtils.isEmpty(attrIdSubAgg)) {
                                ParsedStringTerms attrNameAgg = attrIdSubAgg.get("attrNameAgg");
                                Terms.Bucket attrNameBucket = attrNameAgg.getBuckets().get(0);
                                attrName = attrNameBucket.getKeyAsString();
                            }

                            // 获取attrValue列表
                            List<String> attrValueList = new ArrayList<>();
                            if (!ObjectUtils.isEmpty(attrIdSubAgg)) {
                                ParsedStringTerms attrValueAgg = attrIdSubAgg.get("attrValueAgg");
                                List<? extends Terms.Bucket> attrValueBucketList = attrValueAgg.getBuckets();
                                if (!ObjectUtils.isEmpty(attrValueBucketList)) {
                                    attrValueList = attrValueBucketList.parallelStream()
                                            .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                                            .collect(Collectors.toList());
                                }
                            }

                            // 封装数据
                            attrListVo.setAttrId(attrId);
                            attrListVo.setAttrName(attrName);
                            attrListVo.setAttrValueList(attrValueList);
                            attrListVoList.add(attrListVo);
                        });
                    }
                }
            }
        }
        res.setAttrsList(attrListVoList);

        // 7. 排序
        SearchResponseVo.OrderMapVo orderMapVo = new SearchResponseVo.OrderMapVo();
        orderMapVo.setSort("DESC");
        orderMapVo.setType("1");
        String orderNative = searchParamVo.getOrder();
        if (!StringUtils.isEmpty(orderNative) && !"null".equalsIgnoreCase(orderNative)) {
            String[] split = orderNative.split(":");
            if (!ObjectUtils.isEmpty(split)) {
                String type = split[0];
                String sort = split[1];

                orderMapVo.setSort(sort);
                orderMapVo.setType(type);
            }
        }
        res.setOrderMap(orderMapVo);

        // 8. 分页
        if (pageNo == null || pageNo <= 0) pageNo = 1;
        res.setPageNo(pageNo);
        Long totalPages = (totalHits % 10 == 0 ? (totalHits / 10) : (totalHits / 10L + 1));
        res.setTotalPages(Integer.parseInt(totalPages.toString()));

        // 9. URLParam
        String urlParamString = this.searchParam2URLParamString(searchParamVo);
        res.setUrlParam(urlParamString);

        return res;
    }

    default String searchParam2URLParamString(SearchParamVo searchParamVo) {
        // 从 SearchParamVo 中取出数据
        Long category1Id = searchParamVo.getCategory1Id();
        Long category2Id = searchParamVo.getCategory2Id();
        Long category3Id = searchParamVo.getCategory3Id();
        String keyword = searchParamVo.getKeyword();
        Integer pageNo = searchParamVo.getPageNo();
        String[] props = searchParamVo.getProps();
        String trademark = searchParamVo.getTrademark();

        StringBuilder res = new StringBuilder("/list.html?");
        // 构建 searchParamString
        if (category1Id != null)
            res.append("&category1Id=").append(category1Id);

        if (category2Id != null)
            res.append("&category2Id=").append(category2Id);

        if (category3Id != null)
            res.append("&category3Id=").append(category3Id);

        if (!StringUtils.isEmpty(trademark))
            res.append("&trademark=").append(trademark);

        if (!StringUtils.isEmpty(keyword))
            res.append("&keyword=").append(keyword);

        if (pageNo != null)
            res.append("&pageNo=").append(pageNo);

        if (!ObjectUtils.isEmpty(props)) {
            Arrays.stream(props).parallel().forEach(property -> {
                if (!StringUtils.isEmpty(property)) res.append("&props=").append(property);
            });
        }

        return String.valueOf(res);
    }

}
