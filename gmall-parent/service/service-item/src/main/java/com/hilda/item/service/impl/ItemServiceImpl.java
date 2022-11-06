package com.hilda.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.hilda.item.service.ItemService;
import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.product_feign_client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getSkuDetailById(Long skuId) {
        Map<String, Object> res = new HashMap<>();

        //TODO 根据 skuId 查询 SkuInfo
        SkuInfo skuInfo = productFeignClient.getSkuInfoById(skuId);

        //TODO 查询 SKU 分类信息 （sku_info[只有三级分类]，根据这个三级分类查出所在的一级，二级分类内容，连上三张分类表继续查）
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());

        //TODO 查询 SKU 销售属性列表
        List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrList(skuId, skuInfo.getSpuId());

        //TODO 打包查询信息
        if (!ObjectUtils.isEmpty(skuInfo)){

            res.put("categoryView",categoryView);

            res.put("spuSaleAttrList",spuSaleAttrList);

            //TODO 查询 SKU
            //  查询销售属性值Id 与skuId 组合的map
            Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            //  将这个map 转换为页面需要的Json 对象
            String valueJson = JSON.toJSONString(skuValueIdsMap);
            res.put("valuesSkuJson",valueJson);
        }

        return res;
    }

}
