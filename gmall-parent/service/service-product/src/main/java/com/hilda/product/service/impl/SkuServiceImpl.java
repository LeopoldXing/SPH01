package com.hilda.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.common.constant.RedisConst;
import com.hilda.common.execption.GmallException;
import com.hilda.feign.ProductFeignClient;
import com.hilda.feign.SearchFeignClient;
import com.hilda.model.bean.base.BaseEntity;
import com.hilda.model.bean.product.*;
import com.hilda.model.bean.search.Goods;
import com.hilda.model.vo.product.SkuSaleAttrJsonValueVo;
import com.hilda.product.mapper.*;
import com.hilda.product.service.AttributeService;
import com.hilda.product.service.CategoryService;
import com.hilda.product.service.SkuService;
import com.hilda.product.service.TrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private TrademarkService trademarkService;

    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
        if (skuId == null || skuId <= 0) throw new GmallException("SkuId为空", 100);

        //TODO 查询 SkuInfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        //TODO 封装 SkuInfo
        this.packSkuInfo(skuId, skuInfo, skuImageMapper, skuAttrValueMapper, skuSaleAttrValueMapper);

        return skuInfo;
    }

    @Override
    public Page<SkuInfo> getSkuInfoInPages(Integer current, Integer size) {
        if (current == null || current <= 0) current = 1;
        if (size == null || size <= 0) size = 10;

        Page<SkuInfo> page = new Page<>(current, size);

        return skuInfoMapper.selectPage(page, null);
    }

    @Override
    public List<Long> getSkuIdList() {
        List<Long> skuIdList = new ArrayList<>();
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SkuInfo::getId);
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(queryWrapper);
        if (!ObjectUtils.isEmpty(skuInfoList)) {
            skuIdList = skuInfoList
                    .parallelStream()
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
        }

        return skuIdList;
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

        // 5. 位图更新
        String skuKey = RedisConst.SKUKEY_PREFIX + RedisConst.SKUKEY_SUFFIX + skuInfoId;
        redisTemplate.opsForValue().setBit(skuKey, skuInfoId, true);

        return true;
    }

    @Override
    public Boolean onSale(Long skuId) {
        if (skuId == null || skuId <= 0) return false;
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("指定 SKU 不存在", 500);

        // 将 SKU 对象转换为商品对象
        Goods goods = this.skuInfo2Goods(skuInfo, categoryService, attributeService, trademarkService);

        // elasticsearch上架商品
        searchFeignClient.goodsOnSale(goods);

        skuInfo.setIsSale(1);
        return skuInfoMapper.updateById(skuInfo) > 0;
    }

    @Override
    public Boolean offSale(Long skuId) {
        if (skuId == null || skuId <= 0) return false;
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (ObjectUtils.isEmpty(skuInfo)) throw new GmallException("指定 SKU 不存在", 500);

        // elasticsearch下架商品
        searchFeignClient.goodsOffSale(skuId);

        skuInfo.setIsSale(0);
        return skuInfoMapper.updateById(skuInfo) > 0;
    }

    @Override
    public List<SkuSaleAttrJsonValueVo> getSkuIdListAndValue(Long skuId) {
        return spuSaleAttrMapper.getSpuSaleAttrIdListAndValue(skuId);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuInfo::getId, skuId).select(SkuInfo::getPrice);
        SkuInfo skuInfo = skuInfoMapper.selectOne(queryWrapper);
        return skuInfo.getPrice();
    }

    @Override
    @PostConstruct
    public void initBitmap() {
        List<Long> skuIdList = this.getSkuIdList();
        skuIdList.parallelStream().forEach(skuId -> {
            redisTemplate.opsForValue()
                    .setBit(RedisConst.SKUKEY_PREFIX + RedisConst.SKUKEY_SUFFIX + skuId, skuId, true);
        });
        System.out.println("位图初始化完成");
    }

}
