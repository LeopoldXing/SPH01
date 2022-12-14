package com.hilda.feign;

import com.hilda.model.bean.product.*;
import com.hilda.model.bean.search.SearchAttr;
import com.hilda.model.vo.product.CategoryVo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ProductDegradeFeignClient implements ProductFeignClient {

    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
        return null;
    }

    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId, Long skuId) {
        return null;
    }

    @Override
    public Map<String, Object> getSkuValueIdsMap(Long spuId) {
        return null;
    }

    @Override
    public List<CategoryVo> getCategoryVoList() {
        return null;
    }

    @Override
    public BaseTrademark getTrademarkById(Long trademarkId) {
        return null;
    }

    @Override
    public List<SearchAttr> getSearchAttrListBySkuId(Long skuId) {
        return null;
    }

}
