package com.hilda.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.model.bean.product.BaseCategoryView;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCategoryViewMapper extends BaseMapper<BaseCategoryView> {

    BaseCategoryView getBaseCategoryViewByCategory3Id(Long category3Id);

}
