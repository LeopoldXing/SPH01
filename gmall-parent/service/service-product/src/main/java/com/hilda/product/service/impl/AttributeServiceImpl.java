package com.hilda.product.service.impl;

import com.hilda.model.product.BaseAttrInfo;
import com.hilda.product.mapper.BaseAttrInfoMapper;
import com.hilda.product.mapper.BaseAttrValueMapper;
import com.hilda.product.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
