package com.hilda.weball.controller;

import com.hilda.feign.ProductFeignClient;
import com.hilda.model.vo.product.CategoryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @ApiOperation("渲染首页")
    @GetMapping("/")
    public ModelAndView indexPageRender() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/index/index");

        List<CategoryVo> categoryVoList = productFeignClient.getCategoryVoList();

        modelAndView.addObject("list", categoryVoList);

        return modelAndView;
    }

}
