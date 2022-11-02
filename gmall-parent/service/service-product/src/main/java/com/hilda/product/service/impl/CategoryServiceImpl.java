package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.product.BaseCategory1;
import com.hilda.model.bean.product.BaseCategory2;
import com.hilda.model.bean.product.BaseCategory3;
import com.hilda.product.mapper.*;
import com.hilda.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Override
    public List<BaseCategory1> getFirstLevelCategoryList() {
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getSecondLevelCategoryList(Long category1Id) {
        if (category1Id == null || category1Id <= 0L) throw new GmallException("一级菜单id为空", 100);
        LambdaQueryWrapper<BaseCategory2> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory2::getCategory1Id, category1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseCategory3> getThirdLevelCategoryList(Long category2Id) {
        if (category2Id == null || category2Id <= 0L) throw new GmallException("二级菜单id为空", 100);
        LambdaQueryWrapper<BaseCategory3> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory3::getCategory2Id, category2Id);
        return baseCategory3Mapper.selectList(queryWrapper);
    }

}
