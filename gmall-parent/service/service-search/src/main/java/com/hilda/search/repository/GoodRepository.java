package com.hilda.search.repository;

import com.hilda.model.bean.search.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodRepository extends ElasticsearchRepository<Goods, Long> {
}
