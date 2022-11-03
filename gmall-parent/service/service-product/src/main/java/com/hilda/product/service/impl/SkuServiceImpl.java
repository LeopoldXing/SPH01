package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.common.execption.GmallException;
import com.hilda.model.bean.product.SkuAttrValue;
import com.hilda.model.bean.product.SkuImage;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SkuSaleAttrValue;
import com.hilda.product.mapper.SkuAttrValueMapper;
import com.hilda.product.mapper.SkuImageMapper;
import com.hilda.product.mapper.SkuInfoMapper;
import com.hilda.product.mapper.SkuSaleAttrValueMapper;
import com.hilda.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Override
    public Page<SkuInfo> getSkuInfoInPages(Integer current, Integer size) {
        if (current == null || current <= 0) current = 1;
        if (size == null || size <= 0) size = 10;

        Page<SkuInfo> page = new Page<>(current, size);

        return skuInfoMapper.selectPage(page, null);
    }

    @Override
    @Transactional
    public Boolean addSkuInfo(SkuInfo skuInfo) {
        if (ObjectUtils.isEmpty(skuInfo)) return false;
        Long spuId = skuInfo.getSpuId();

        // 1. 插入SKU详情
        // 默认下架
        skuInfo.setIsSale(0);
        if (skuInfoMapper.insert(skuInfo) <= 0) throw new GmallException("SKU插入失败", 300);
        Long skuInfoId = skuInfo.getId();
        if (skuInfoId == null || skuInfoId <= 0) throw new GmallException("SKU id回写失败", 200);

        // 2. 插入SKU 平台属性值列表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(!ObjectUtils.isEmpty(skuAttrValueList)) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfoId);
                if (skuAttrValueMapper.insert(skuAttrValue) <= 0) throw new GmallException("SKU 平台属性插入失败", 301);
            }
        }

        // 3. 插入 SKU 销售属性值列表
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(!ObjectUtils.isEmpty(skuSaleAttrValueList)) {
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfoId);
                skuSaleAttrValue.setSpuId(spuId);
                if (skuSaleAttrValueMapper.insert(skuSaleAttrValue) <= 0) throw new GmallException("SKU 销售属性插入失败", 302);
            }
        }

        // 4. 插入 SKU 图片信息列表
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(!ObjectUtils.isEmpty(skuImageList)){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfoId);
                if (skuImageMapper.insert(skuImage) <= 0) throw new GmallException("SKU 图片信息插入失败", 303);
            }
        }

        return true;
    }

    @Override
    public Boolean onSale(Long skuId) {
        if (skuId == null || skuId <= 0) return false;
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("指定 SKU 不存在", 500);

        skuInfo.setIsSale(1);
        return skuInfoMapper.updateById(skuInfo) > 0;
    }

    @Override
    public Boolean offSale(Long skuId) {
        if (skuId == null || skuId <= 0) return false;
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("指定 SKU 不存在", 500);

        skuInfo.setIsSale(0);
        return skuInfoMapper.updateById(skuInfo) > 0;
    }

}
