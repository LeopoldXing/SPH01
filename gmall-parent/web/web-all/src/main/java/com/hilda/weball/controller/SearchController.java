package com.hilda.weball.controller;

import com.hilda.feign.SearchFeignClient;
import com.hilda.model.vo.search.SearchParamVo;
import com.hilda.model.vo.search.SearchResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Api("搜索相关接口")
@Controller
public class SearchController {

    @Autowired
    private SearchFeignClient searchFeignClient;

    @ApiOperation("渲染搜索结果页面")
    @GetMapping("/list.html")
    public ModelAndView searchResultRender(SearchParamVo searchParamVo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list/index");

        // 获取搜索结果
        SearchResponseVo searchResponseVo = searchFeignClient.searchGoodsByConditions(searchParamVo).getData()/*new SearchResponseVo()*/;

        // 返回参数
        modelAndView.addObject("searchParam", searchParamVo);
        modelAndView.addObject("trademarkParam", searchResponseVo.getTrademarkParam());
        modelAndView.addObject("trademarkList", searchResponseVo.getTrademarkList());
        modelAndView.addObject("propsParamList", searchResponseVo.getPropsParamList());
        modelAndView.addObject("attrsList", searchResponseVo.getAttrsList());
        modelAndView.addObject("orderMap", searchResponseVo.getOrderMap());
        modelAndView.addObject("goodsList", searchResponseVo.getGoodsList());
        modelAndView.addObject("pageNo", searchResponseVo.getPageNo());
        modelAndView.addObject("urlParam", searchResponseVo.getUrlParam());
        modelAndView.addObject("totalPages", searchResponseVo.getTotalPages());

        return modelAndView;
    }

}
