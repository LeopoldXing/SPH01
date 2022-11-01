package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hilda.common.execption.GmallException;
import com.hilda.model.product.BaseAttrInfo;
import com.hilda.model.product.BaseAttrValue;
import com.hilda.product.mapper.BaseAttrInfoMapper;
import com.hilda.product.mapper.BaseAttrValueMapper;
import com.hilda.product.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        List<BaseAttrInfo> res = baseAttrInfoMapper.getBaseAttrInfoListByCategoryIds(category1Id, category2Id, category3Id);
        return res;
    }

    @Override
    public Boolean saveBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        int res = 0;

        if (ObjectUtils.isEmpty(baseAttrInfo)) return true;
        Long attrId = baseAttrInfo.getId();

        if (attrId == null || attrId == 0) {
            // 添加平台属性
            res = baseAttrInfoMapper.insert(baseAttrInfo);

            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            attrValueList.parallelStream().forEach(baseAttrValue -> {
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                if (baseAttrValueMapper.insert(baseAttrValue) < 0) throw new GmallException("平台属性插入失败", 101);
            });
        } else {
            // 修改平台属性
            if (baseAttrInfoMapper.updateById(baseAttrInfo) <= 0) return false;

            List<BaseAttrValue> updateAttrValueList = baseAttrInfo.getAttrValueList();

            /*
            // 删除该平台属性的所有属性值
            LambdaQueryWrapper<BaseAttrValue> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BaseAttrValue::getAttrId, attrId);
            baseAttrValueMapper.delete(queryWrapper);

            // 重新插入该平台属性的属性值
            updateAttrValueList.parallelStream().forEach(baseAttrValue -> {
                baseAttrValue.setAttrId(attrId);
                if (baseAttrValueMapper.insert(baseAttrValue) < 0) throw new GmallException("平台属性插入失败", 101);
            });
            */

            // 删除平台属性值
            LambdaQueryWrapper<BaseAttrValue> deleteQueryWrapper = new LambdaQueryWrapper<>();
            if (updateAttrValueList.size() == 0) {
                // 全部删除
                deleteQueryWrapper.eq(BaseAttrValue::getAttrId, attrId);
            } else {
                // 部分删除
                deleteQueryWrapper
                        .notIn(BaseAttrValue::getId,
                                updateAttrValueList.parallelStream().map(baseAttrValue -> {
                                    Long id = baseAttrValue.getId();
                                    if (id != null && id > 0) return id;
                                    else return 0;
                                }).collect(Collectors.toList()))
                        .eq(BaseAttrValue::getAttrId, attrId);
            }
            res = baseAttrValueMapper.delete(deleteQueryWrapper);

            // 添加或修改平台属性值
            updateAttrValueList.parallelStream().forEach(baseAttrValue -> {
                Long id = baseAttrValue.getId();
                if (id == null || id == 0) {
                    // 添加新的平台属性值
                    baseAttrValue.setAttrId(attrId);
                    if (baseAttrValueMapper.insert(baseAttrValue) <= 0) throw new GmallException("平台属性插入失败", 101);
                } else {
                    // 更新平台属性值
                    if (baseAttrValueMapper.updateById(baseAttrValue) <= 0) throw new GmallException("平台属性信息更新失败", 102);
                }
            });
        }

        return res > 0;
    }

    @Override
    public BaseAttrInfo getAttrInfoById(Long id) {
        if (id == null || id <= 0) throw new GmallException("平台属性id为空", 100);
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(id);
        this.packBaseAttrInfo(baseAttrInfo, baseAttrValueMapper);
        return baseAttrInfo;
    }

}
