package com.hilda.search.service.impl;

import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.search.Goods;
import com.hilda.search.repository.GoodRepository;
import com.hilda.search.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodRepository goodRepository;

    @Override
    public Boolean onSale(Goods goods) {
        if (ObjectUtils.isEmpty(goods)) throw new GmallException("上架商品为空", 100);

        goodRepository.save(goods);
        return true;
    }

    @Override
    public Boolean offSale(Long goodsId) {
        if (goodsId == null) return false;
        goodRepository.deleteById(goodsId);
        return true;
    }

}
