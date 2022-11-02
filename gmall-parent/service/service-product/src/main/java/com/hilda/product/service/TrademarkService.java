package com.hilda.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hilda.model.product.BaseTrademark;

public interface TrademarkService {

    BaseTrademark getTrademarkById(Long id);

    IPage<BaseTrademark> getTrademarkInPages(Integer current, Integer size);

    Boolean addTrademark(BaseTrademark baseTrademark);

    Boolean updateTrademark(BaseTrademark baseTrademark);

    Boolean deleteTrademarkById(Long id);

}
