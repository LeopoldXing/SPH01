package com.hilda.item.service;

import com.hilda.model.vo.item.SkuDetailVo;

public interface ItemService {

    /**
     * 根据 SkuId 获取 SKU详细信息
     * SKU详细信息包括：
     * 1. SkuInfo
     * 2. Sku图片信息（sku的默认图片[sku_info]，sku_image[一组图片]）
     * 3，Sku分类信息（sku_info[只有三级分类]，根据这个三级分类查出所在的一级，二级分类内容，连上三张分类表继续查）
     * 4，Sku销售属性相关信息（查出自己的sku组合，还要查出这个sku所在的spu定义了的所有销售属性和属性值）
     * 5，Sku价格信息（平台可以单独修改价格，sku后续会放入缓存，为了回显最新价格，所以单独获取）
     * @param skuId
     * @return
     */
    SkuDetailVo getSkuDetailBySkuId(Long skuId);

}
