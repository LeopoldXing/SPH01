package com.hilda.weball.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Api("登录接口")
@Controller
public class LoginController {

    @ApiOperation("渲染登录页")
    @GetMapping("/login.html")
    public ModelAndView loginPageRender(@RequestParam("originUrl") String originUrl) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        // 包装页面所需的数据
        modelAndView.addObject("originUrl", originUrl);

        return modelAndView;
    }

}
