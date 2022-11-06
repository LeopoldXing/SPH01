package com.hilda.product_feign_client;

import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.product.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfoById(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/product/inner/getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable("category3Id")Long category3Id);

    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable(value = "skuId") Long skuId);

    @GetMapping("/api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    List<SpuSaleAttr> getSpuSaleAttrList(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId);

    @GetMapping("/api/product/inner/getSkuValueIdsMap/{spuId}")
    Map<String, Object> getSkuValueIdsMap(@PathVariable("spuId") Long spuId);

    /**
     * 查询首页分类信息
     * @return
     */
    @GetMapping("/api/product/inner/getBaseCategoryList")
    List<CategoryVo> getCategoryVoList();

}
