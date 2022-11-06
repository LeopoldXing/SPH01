package com.hilda.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.model.bean.product.BaseCategory3;
import com.hilda.model.vo.product.CategoryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseCategory3Mapper extends BaseMapper<BaseCategory3> {

    List<CategoryVo> getCategoryVoList();

}
