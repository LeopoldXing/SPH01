package com.hilda.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.model.bean.product.BaseAttrInfo;
import com.hilda.model.bean.search.SearchAttr;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> getBaseAttrInfoListByCategoryIds (@Param("category1Id") Long category1Id,
                                                         @Param("category2Id") Long category2Id,
                                                         @Param("category3Id") Long category3Id);

    List<SearchAttr> getSearchAttrList(@Param("skuId") Long skuId);
}
