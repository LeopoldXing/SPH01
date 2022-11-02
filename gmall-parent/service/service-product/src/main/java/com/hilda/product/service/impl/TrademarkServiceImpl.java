package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.product.BaseTrademark;
import com.hilda.product.mapper.TrademarkMapper;
import com.hilda.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TrademarkServiceImpl implements TrademarkService {

    @Autowired
    private TrademarkMapper trademarkMapper;

    @Override
    public BaseTrademark getTrademarkById(Long id) {
        return trademarkMapper.selectById(id);
    }

    @Override
    public IPage<BaseTrademark> getTrademarkInPages(Integer current, Integer size) {
        if (current == null || current <= 0) current = 1;
        if (size == null || size <= 0) size = 10;

        Page<BaseTrademark> page = new Page<>(current, size);

        Page<BaseTrademark> baseTrademarkPage = trademarkMapper.selectPage(page, null);

        return baseTrademarkPage;
    }

    @Override
    public Boolean addTrademark(BaseTrademark baseTrademark) {
        if (ObjectUtils.isEmpty(baseTrademark)) return false;
        if (StringUtils.isEmpty(baseTrademark.getTmName())) return false;

        return trademarkMapper.insert(baseTrademark) > 0;
    }

    @Override
    public Boolean updateTrademark(BaseTrademark baseTrademark) {
        if (ObjectUtils.isEmpty(baseTrademark)) return false;
        Long id = baseTrademark.getId();
        if (id == null || id <= 0) throw new GmallException("商标id为空", 100);
        return trademarkMapper.updateById(baseTrademark) > 0;
    }

    @Override
    public Boolean deleteTrademarkById(Long id) {
        if (id == null) return false;
        trademarkMapper.deleteById(id);
        return true;
    }

    @Override
    public List<BaseTrademark> getTrademarkList() {
        List<BaseTrademark> baseTrademarkList = trademarkMapper.selectList(null);
        return baseTrademarkList;
    }

}
