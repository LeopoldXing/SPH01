package com.hilda.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.hilda.common.execption.GmallException;
import com.hilda.item.service.CacheService;
import com.hilda.item.service.ItemService;
import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.item.SkuDetailVo;
import com.hilda.feign.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private CacheService cacheService;

    @Override
    public SkuDetailVo getSkuDetailBySkuId(Long skuId) {
        // 从 Redis 缓存中取出数据
        SkuDetailVo skuDetailVo = cacheService.getSkuDetailVoFromCache(skuId);
        if (!ObjectUtils.isEmpty(skuDetailVo)) {
            // 缓存命中
            return skuDetailVo;
        } else {
            // 缓存未命中
            // 1. 根据位图判断是否存在
            Boolean bitmapRes = cacheService.checkSkuDetailVoExistenceByBitmap(skuId);
            if (bitmapRes) {
                // 存在
                // 查询skuDetailVo
                SkuDetailVo detailVo = getSkuDetailVo(skuId);
                // 将查询结果存入缓存
                cacheService.saveSkuDetailVoCache(detailVo);
                return detailVo;
            }
        }
        // 缓存未命中，位图查询也没有
        return null;
    }

    private SkuDetailVo getSkuDetailVo(Long skuId) {
        SkuDetailVo res = new SkuDetailVo();

        //TODO 根据 skuId 查询 SkuInfo
        SkuInfo skuInfo = productFeignClient.getSkuInfoById(skuId);
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("指定skuId的 SKU 不存在", 500);
        res.setSkuInfo(skuInfo);

        //TODO 查询 SKU 分类信息 （sku_info[只有三级分类]，根据这个三级分类查出所在的一级，二级分类内容，连上三张分类表继续查）
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        res.setCategoryView(categoryView);

        //TODO 查询 SPU 销售属性列表
        List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrList(skuInfo.getSpuId(), skuId);
        res.setSpuSaleAttrList(spuSaleAttrList);

        //TODO 查询销售属性值Id 与skuId 组合的map
        Map<String, Object> map = productFeignClient.getSkuValueIdsMap(skuId);
        if (ObjectUtils.isEmpty(map)) res.setValuesSkuJson("");
        else res.setValuesSkuJson(JSON.toJSONString(map));

        //TODO 查询 SKU 价格
        res.setPrice(productFeignClient.getSkuPrice(skuId));

        return res;
    }

}
