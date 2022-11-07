package com.hilda.weball.controller.render;

import com.hilda.Item_feign_client.ItemFeignClient;
import com.hilda.model.vo.item.SkuDetailVo;
import com.hilda.model.vo.product.CategoryVo;
import com.hilda.product_feign_client.ProductFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api("通用页面渲染器")
@Controller
public class GenericPageRender {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @ApiOperation("渲染首页")
    @GetMapping("/")
    public ModelAndView indexPageRender(ModelAndView modelAndView) {
        modelAndView.setViewName("/index/index");

        List<CategoryVo> categoryVoList = productFeignClient.getCategoryVoList();

        modelAndView.addObject("list", categoryVoList);

        return modelAndView;
    }

    @ApiOperation("渲染商品详情页")
    @GetMapping("/{skuId}.html")
    public ModelAndView itemPageRender(ModelAndView modelAndView, @PathVariable("skuId") Long skuId) {
        modelAndView.setViewName("item/index");

        SkuDetailVo skuDetailVo = itemFeignClient.getSkuDetailBySkuId(skuId);

        modelAndView.addObject("categoryView", skuDetailVo.getCategoryView());
        modelAndView.addObject("skuInfo", skuDetailVo.getSkuInfo());
        modelAndView.addObject("spuSaleAttrList", skuDetailVo.getSpuSaleAttrList());
        modelAndView.addObject("valuesSkuJson", skuDetailVo.getValuesSkuJson());
        modelAndView.addObject("price", skuDetailVo.getPrice());

        return modelAndView;
    }

}
