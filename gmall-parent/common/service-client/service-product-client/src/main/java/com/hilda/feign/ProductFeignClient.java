package com.hilda.feign;

import com.hilda.model.bean.product.*;
import com.hilda.model.bean.search.SearchAttr;
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

    @GetMapping("/api/product/inner/getSpuSaleAttrList/{spuId}/{skuId}")
    List<SpuSaleAttr> getSpuSaleAttrList(@PathVariable("spuId") Long spuId, @PathVariable("skuId") Long skuId);

    @GetMapping("/api/product/inner/getSkuValueIdsMap/{skuId}")
    Map<String, Object> getSkuValueIdsMap(@PathVariable("skuId") Long skuId);

    /**
     * 查询首页分类信息
     * @return
     */
    @GetMapping("/api/product/inner/getBaseCategoryList")
    List<CategoryVo> getCategoryVoList();

    @GetMapping("/getTrademarkById/{trademarkId}")
    BaseTrademark getTrademarkById(@PathVariable("trademarkId") Long trademarkId);

    @GetMapping("/getSearchAttrList/{skuId}")
    public List<SearchAttr> getSearchAttrListBySkuId(@PathVariable("skuId") Long skuId);

}
