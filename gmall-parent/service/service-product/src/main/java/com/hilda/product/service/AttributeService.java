package com.hilda.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hilda.common.execption.GmallException;
import com.hilda.model.product.BaseAttrInfo;
import com.hilda.model.product.BaseAttrValue;
import com.hilda.product.mapper.BaseAttrValueMapper;

import java.util.List;

public interface AttributeService {

    List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id);

    Boolean saveBaseAttrInfo(BaseAttrInfo baseAttrInfo);

    BaseAttrInfo getAttrInfoById(Long id);

    /**
     * 根据三个级别的的categoryId来计算其的层级
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    default int calculateCategoryLevel(Long category1Id, Long category2Id, Long category3Id) {
        if (category1Id == null || category1Id <= 0L) throw new GmallException("一级菜单id为空", 100);
        if (category3Id != null && category3Id > 0L) {
            if (category2Id == null || category2Id <= 0L) throw new GmallException("存在三级菜单，但二级菜单id为空", 100);
            return 3;
        } else if (category2Id != null && category2Id > 0L) {
            return 2;
        } else return 1;
    }

    /**
     * 打包BaseAttrInfo
     * @param baseAttrInfo
     * @return
     */
    default void packBaseAttrInfo (BaseAttrInfo baseAttrInfo, BaseAttrValueMapper baseAttrValueMapper) {
        if (baseAttrInfo == null || baseAttrValueMapper == null) return;
        Long attr_id = baseAttrInfo.getId();
        LambdaQueryWrapper<BaseAttrValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseAttrValue::getAttrId, attr_id);
        baseAttrInfo.setAttrValueList(baseAttrValueMapper.selectList(queryWrapper));
    }
}
