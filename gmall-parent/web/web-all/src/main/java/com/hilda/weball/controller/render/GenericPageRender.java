package com.hilda.weball.controller.render;

import com.hilda.model.vo.product.CategoryVo;
import com.hilda.product_feign_client.ProductFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api("通用页面渲染器")
@Controller
public class GenericPageRender {

    @Autowired
    private ProductFeignClient productFeignClient;

    @ApiOperation("渲染首页")
    @GetMapping("/")
    public ModelAndView indexPageRender(ModelAndView modelAndView) {
        modelAndView.setViewName("/index/index");

        List<CategoryVo> categoryVoList = productFeignClient.getCategoryVoList();

        modelAndView.addObject("list", categoryVoList);

        return modelAndView;
    }

//    @ApiOperation("渲染商品详情页")
//    @GetMapping("")

}
