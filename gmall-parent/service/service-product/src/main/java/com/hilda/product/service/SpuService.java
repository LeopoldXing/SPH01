package com.hilda.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.model.bean.product.BaseSaleAttr;
import com.hilda.model.bean.product.SpuImage;
import com.hilda.model.bean.product.SpuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.product.SpuInfoVo;

import java.util.List;

public interface SpuService {

    Page<SpuInfo> getSPUInfoListByCategory3IdInPages(Integer current, Integer size, Long category3Id);

    List<BaseSaleAttr> getBaseSaleAttrList();

    Boolean addSpu(SpuInfoVo spuInfoVo);

    List<SpuImage> getImageListBySpuId(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(Long spuId);
}
