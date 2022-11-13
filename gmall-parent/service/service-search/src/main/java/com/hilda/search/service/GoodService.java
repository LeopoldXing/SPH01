package com.hilda.search.service;

import com.hilda.model.bean.search.Goods;

public interface GoodService {

    Boolean onSale(Goods goods);

    Boolean offSale(Long goodsId);

}
