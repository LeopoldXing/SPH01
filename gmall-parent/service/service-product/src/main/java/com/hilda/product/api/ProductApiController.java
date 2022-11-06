package com.hilda.product.api;

import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.product.CategoryVo;
import com.hilda.product.service.CategoryService;
import com.hilda.product.service.SkuService;
import com.hilda.product.service.SpuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api("商品相关 RPC 接口")
@RestController
@RequestMapping("/api/product/inner")
public class ProductApiController {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfoById(@PathVariable("skuId") Long skuId){
        SkuInfo skuInfoById = skuService.getSkuInfoById(skuId);

        return skuInfoById;
    }

    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id")Long category3Id){
        BaseCategoryView categoryView = categoryService.getCategoryViewByCategory3Id(category3Id);

        return categoryView;
    }

    @GetMapping("/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrList(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuService.getSpuSaleAttrListBySpuId(spuId);

        return spuSaleAttrList;
    }

    @GetMapping("/getBaseCategoryList")
    public List<CategoryVo> getCategoryVoList() {
        List<CategoryVo> categoryVoList = categoryService.getCategoryVoList();

        return categoryVoList == null ? new ArrayList<>() : categoryVoList;
    }

}
