package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.product.*;
import com.hilda.model.vo.product.SpuInfoVo;
import com.hilda.product.mapper.BaseSaleAttrMapper;
import com.hilda.product.mapper.SpuImageMapper;
import com.hilda.product.mapper.SpuInfoMapper;
import com.hilda.product.mapper.SpuSaleAttrValueMapper;
import com.hilda.product.service.SpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public Page<SpuInfo> getSPUInfoListByCategory3IdInPages(Integer current, Integer size, Long category3Id) {
        if (current == null || current <= 0) current = 1;
        if (size == null || size <= 0) size = 10;

        Page<SpuInfo> pageConfig = new Page<>(current, size);

        LambdaQueryWrapper<SpuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SpuInfo::getCategory3Id, category3Id);

        Page<SpuInfo> res = spuInfoMapper.selectPage(pageConfig, queryWrapper);

        return res;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    @Transactional
    public Boolean addSpu(SpuInfoVo spuInfoVo) {
        if (ObjectUtils.isEmpty(spuInfoVo)) return false;
        List<SpuImage> spuImageList = spuInfoVo.getSpuImageList();
        List<SpuInfoVo.SpuSaleAttrListDTO> spuSaleAttrList = spuInfoVo.getSpuSaleAttrList();
        if (spuImageList == null || spuSaleAttrList == null) return false;

        // 1. 保存SpuInfo
        SpuInfo spuInfo = new SpuInfo();
        BeanUtils.copyProperties(spuInfoVo, spuInfo);
        if (spuInfoMapper.insert(spuInfo) <= 0) throw new GmallException("SPU保存失败", 300);

        Long spuId = spuInfo.getId();
        if (spuId == null || spuId <= 0) throw new GmallException("SpuId回写失败", 301);

        // 2. 保存 图片列表
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuId);
            if (spuImageMapper.insert(spuImage) <= 0) throw new GmallException("图片地址保存失败", 302);
        }

        // 3. 保存 销售属性列表
        for (SpuInfoVo.SpuSaleAttrListDTO spuSaleAttrListDTO : spuSaleAttrList) {
            // 保存 属性名
            SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
            BeanUtils.copyProperties(spuSaleAttrListDTO, spuSaleAttr);

            // 保存 属性值列表
            List<SpuInfoVo.SpuSaleAttrListDTO.SpuSaleAttrValueListDTO> spuSaleAttrValueList = spuSaleAttrListDTO.getSpuSaleAttrValueList();
            spuSaleAttrValueList.stream().forEach(spuSaleAttrValueListDTO -> {
                SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
                BeanUtils.copyProperties(spuSaleAttrValueListDTO, spuSaleAttrValue);
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                if (spuSaleAttrValueMapper.insert(spuSaleAttrValue) <= 0) throw new GmallException("属性值保存失败", 303);
            });
        }

        return true;
    }

}
