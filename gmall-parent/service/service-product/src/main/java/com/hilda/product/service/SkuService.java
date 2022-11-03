package com.hilda.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.model.bean.product.SkuInfo;

public interface SkuService extends IService<SkuInfo> {

    Page<SkuInfo> getSkuInfoInPages(Integer current, Integer size);

    Boolean addSkuInfo(SkuInfo skuInfo);

    Boolean onSale(Long skuId);

    Boolean offSale(Long skuId);

}
