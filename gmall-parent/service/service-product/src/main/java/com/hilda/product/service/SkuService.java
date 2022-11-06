package com.hilda.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.model.bean.product.SkuAttrValue;
import com.hilda.model.bean.product.SkuImage;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SkuSaleAttrValue;
import com.hilda.product.mapper.SkuAttrValueMapper;
import com.hilda.product.mapper.SkuImageMapper;
import com.hilda.product.mapper.SkuSaleAttrValueMapper;
import org.springframework.util.ObjectUtils;

import java.util.List;

public interface SkuService extends IService<SkuInfo> {

    SkuInfo getSkuInfoById(Long skuId);

    Page<SkuInfo> getSkuInfoInPages(Integer current, Integer size);

    Boolean addSkuInfo(SkuInfo skuInfo);

    Boolean onSale(Long skuId);

    Boolean offSale(Long skuId);

    default void packSkuInfo(Long skuId, SkuInfo skuInfo,
                             SkuImageMapper skuImageMapper,
                             SkuAttrValueMapper skuAttrValueMapper,
                             SkuSaleAttrValueMapper skuSaleAttrValueMapper) {
        if (ObjectUtils.isEmpty(skuInfo) || ObjectUtils.isEmpty(skuId)) return;

        //TODO 查询 SKU图片列表
        LambdaQueryWrapper<SkuImage> queryWrapperImage = new LambdaQueryWrapper<>();
        queryWrapperImage.eq(SkuImage::getSkuId, skuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(queryWrapperImage);
        skuInfo.setSkuImageList(skuImageList);

        //TODO 查询 SKU平台属性值列表
        LambdaQueryWrapper<SkuAttrValue> queryWrapperAttr = new LambdaQueryWrapper<>();
        queryWrapperAttr.eq(SkuAttrValue::getSkuId, skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(queryWrapperAttr);
        skuInfo.setSkuAttrValueList(skuAttrValueList);

        //TODO 查询 SKU销售属性值列表
        LambdaQueryWrapper<SkuSaleAttrValue> queryWrapperSaleAttr = new LambdaQueryWrapper<>();
        queryWrapperSaleAttr.eq(SkuSaleAttrValue::getSkuId, skuId);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectList(queryWrapperSaleAttr);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
    }

}
