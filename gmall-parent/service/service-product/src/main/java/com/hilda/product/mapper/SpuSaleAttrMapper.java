package com.hilda.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.model.bean.product.BaseSaleAttr;
import com.hilda.model.bean.product.SpuSaleAttr;
import com.hilda.model.vo.product.SkuSaleAttrJsonValueVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpuSaleAttrMapper extends BaseMapper<BaseSaleAttr> {

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId_isChecked(@Param("spuId") Long spuId, @Param("skuId") Long skuId);

    List<SkuSaleAttrJsonValueVo> getSpuSaleAttrIdListAndValue(@Param("skuId") Long skuId);

}
