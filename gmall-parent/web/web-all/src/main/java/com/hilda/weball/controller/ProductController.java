package com.hilda.weball.controller;

import com.hilda.feign.ItemFeignClient;
import com.hilda.model.vo.item.SkuDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Api("商品相关接口")
@Controller
public class ProductController {

    @Autowired
    private ItemFeignClient itemFeignClient;

    @ApiOperation("渲染商品详情页")
    @GetMapping("/{skuId}.html")
    public ModelAndView itemPageRender(@PathVariable("skuId") Long skuId) {
        ModelAndView modelAndView = new ModelAndView();
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
