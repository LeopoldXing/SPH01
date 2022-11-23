package com.hilda.product.api;

import com.hilda.model.bean.product.*;
import com.hilda.model.bean.search.SearchAttr;
import com.hilda.model.vo.product.CategoryVo;
import com.hilda.model.vo.product.SkuSaleAttrJsonValueVo;
import com.hilda.product.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("商品相关 RPC 接口")
@RestController
@RequestMapping("/api/product/inner")
public class ProductApiController {

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TrademarkService trademarkService;

    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfoById(@PathVariable("skuId") Long skuId){
        SkuInfo skuInfoById = skuService.getSkuInfoById(skuId);
        return skuInfoById;
    }

    @GetMapping("/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable("skuId") Long skuId) {
        BigDecimal price = skuService.getSkuPrice(skuId);
        return price;
    }

    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id")Long category3Id){
        BaseCategoryView categoryView = categoryService.getCategoryViewByCategory3Id(category3Id);
        return categoryView;
    }

    @GetMapping("/getSpuSaleAttrList/{spuId}/{skuId}")
    public List<SpuSaleAttr> getSpuSaleAttrList(@PathVariable("spuId") Long spuId, @PathVariable("skuId") Long skuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuService.getSpuSaleAttrListBySpuId_SkuId(spuId, skuId);
        return spuSaleAttrList == null ? new ArrayList<>() : spuSaleAttrList;
    }

    @GetMapping("/getBaseCategoryList")
    public List<CategoryVo> getCategoryVoList() {
        List<CategoryVo> categoryVoList = categoryService.getCategoryVoList();
        return categoryVoList == null ? new ArrayList<>() : categoryVoList;
    }

    @GetMapping("/getSkuValueIdsMap/{skuId}")
    public Map<String, Object> getSkuValueIdsMap(@PathVariable("skuId") Long skuId){
        Map<String, Object> res = new HashMap<>();

        List<SkuSaleAttrJsonValueVo> skuSaleAttrJsonValueVoList = skuService.getSkuIdListAndValue(skuId);
        for (SkuSaleAttrJsonValueVo skuSaleAttrJsonValueVo : skuSaleAttrJsonValueVoList) {
            String jsonValue = skuSaleAttrJsonValueVo.getJsonValue();
            if (!StringUtils.isEmpty(jsonValue)) {
                res.put(jsonValue , skuSaleAttrJsonValueVo.getSkuId());
            }
        }
        return res;
    }

    @GetMapping("/getTrademarkById/{trademarkId}")
    public BaseTrademark getTrademarkById(@PathVariable("trademarkId") Long trademarkId) {
        return trademarkService.getTrademarkById(trademarkId);
    }

    @GetMapping("/getSearchAttrList/{skuId}")
    public List<SearchAttr> getSearchAttrListBySkuId(@PathVariable("skuId") Long skuId) {
        return attributeService.getSearchAttrList(skuId);
    }

}
