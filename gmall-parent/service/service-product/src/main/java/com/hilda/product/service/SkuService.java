package com.hilda.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.product.*;
import com.hilda.model.bean.search.Goods;
import com.hilda.model.bean.search.SearchAttr;
import com.hilda.model.vo.product.SkuSaleAttrJsonValueVo;
import com.hilda.product.mapper.SkuAttrValueMapper;
import com.hilda.product.mapper.SkuImageMapper;
import com.hilda.product.mapper.SkuSaleAttrValueMapper;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface SkuService extends IService<SkuInfo> {

    SkuInfo getSkuInfoById(Long skuId);

    Page<SkuInfo> getSkuInfoInPages(Integer current, Integer size);

    List<Long> getSkuIdList();

    Boolean addSkuInfo(SkuInfo skuInfo);

    Boolean onSale(Long skuId);

    Boolean offSale(Long skuId);

    List<SkuSaleAttrJsonValueVo> getSkuIdListAndValue(Long skuId);

    BigDecimal getSkuPrice(Long skuId);

    void initBitmap();

    default void packSkuInfo(Long skuId, SkuInfo skuInfo,
                             SkuImageMapper skuImageMapper,
                             SkuAttrValueMapper skuAttrValueMapper,
                             SkuSaleAttrValueMapper skuSaleAttrValueMapper) {
        if (ObjectUtils.isEmpty(skuInfo) || ObjectUtils.isEmpty(skuId)) return;

        // 1. 查询 SKU图片列表
        LambdaQueryWrapper<SkuImage> queryWrapperImage = new LambdaQueryWrapper<>();
        queryWrapperImage.eq(SkuImage::getSkuId, skuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(queryWrapperImage);
        skuInfo.setSkuImageList(skuImageList);

        // 2. 查询 SKU平台属性值列表
        LambdaQueryWrapper<SkuAttrValue> queryWrapperAttr = new LambdaQueryWrapper<>();
        queryWrapperAttr.eq(SkuAttrValue::getSkuId, skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(queryWrapperAttr);
        skuInfo.setSkuAttrValueList(skuAttrValueList);

        // 3. 查询 SKU销售属性值列表
        LambdaQueryWrapper<SkuSaleAttrValue> queryWrapperSaleAttr = new LambdaQueryWrapper<>();
        queryWrapperSaleAttr.eq(SkuSaleAttrValue::getSkuId, skuId);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectList(queryWrapperSaleAttr);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
    }

    default Goods skuInfo2Goods(SkuInfo skuInfo, CategoryService categoryService, AttributeService attributeService, TrademarkService trademarkService) {
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("skuInfo为空", 100);

        Long skuId = skuInfo.getId();

        Goods goods = new Goods();
        goods.setId(skuId);

        // 查询分类信息
        Long category3Id = skuInfo.getCategory3Id();
        BaseCategoryView categoryView = categoryService.getCategoryViewByCategory3Id(category3Id);
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(category3Id);
        goods.setCategory3Name(categoryView.getCategory3Name());

        // 查询平台属性列表
        List<SearchAttr> searchAttrList = attributeService.getSearchAttrList(skuId);
        goods.setAttrs(searchAttrList);

        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setHotScore(0L);
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setTitle(skuInfo.getSkuName());

        // 查询品牌信息
        Long tmId = skuInfo.getTmId();
        BaseTrademark trademark = trademarkService.getTrademarkById(tmId);
        goods.setTmId(tmId);
        goods.setTmLogoUrl(trademark.getLogoUrl());
        goods.setTmName(trademark.getTmName());

        goods.setCreateTime(new Date());

        return goods;
    }
}
