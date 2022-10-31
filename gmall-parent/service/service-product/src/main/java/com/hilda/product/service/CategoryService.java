package com.hilda.product.service;

import com.hilda.model.product.BaseCategory1;
import com.hilda.model.product.BaseCategory2;
import com.hilda.model.product.BaseCategory3;

import java.util.List;

public interface CategoryService {

    List<BaseCategory1> getFirstLevelCategoryList();

    List<BaseCategory2> getSecondLevelCategoryList(Long category1Id);

    List<BaseCategory3> getThirdLevelCategoryList(Long category2Id);

}
