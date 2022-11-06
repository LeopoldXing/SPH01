package com.hilda.product.service;

import com.hilda.model.bean.product.BaseCategory1;
import com.hilda.model.bean.product.BaseCategory2;
import com.hilda.model.bean.product.BaseCategory3;
import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.vo.product.CategoryVo;

import java.util.List;

public interface CategoryService {

    List<BaseCategory1> getFirstLevelCategoryList();

    List<BaseCategory2> getSecondLevelCategoryList(Long category1Id);

    List<BaseCategory3> getThirdLevelCategoryList(Long category2Id);

    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);

    List<CategoryVo> getCategoryVoList();

}
