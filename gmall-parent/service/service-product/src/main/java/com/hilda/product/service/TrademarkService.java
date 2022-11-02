package com.hilda.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hilda.model.bean.product.BaseTrademark;

import java.util.List;

public interface TrademarkService {

    BaseTrademark getTrademarkById(Long id);

    IPage<BaseTrademark> getTrademarkInPages(Integer current, Integer size);

    Boolean addTrademark(BaseTrademark baseTrademark);

    Boolean updateTrademark(BaseTrademark baseTrademark);

    Boolean deleteTrademarkById(Long id);

    List<BaseTrademark> getTrademarkList();

}
