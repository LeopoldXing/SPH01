package com.hilda.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.model.bean.product.BaseSaleAttr;
import com.hilda.model.bean.product.SpuSaleAttr;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpuSaleAttrMapper extends BaseMapper<BaseSaleAttr> {

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(Long spuId);

}
