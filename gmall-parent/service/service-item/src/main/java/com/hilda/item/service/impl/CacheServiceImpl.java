package com.hilda.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.hilda.item.service.CacheService;
import com.hilda.model.vo.item.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SkuDetailVo getSkuDetailVoFromCache(Long skuId) {
        String skuKey = this.getSkuKeyById(skuId);
        String skuDetailVoJson = stringRedisTemplate.opsForValue().get(skuKey);
        SkuDetailVo skuDetailVo = null;
        if (!StringUtils.isEmpty(skuDetailVoJson)) {
            try {
                skuDetailVo = JSON.parseObject(skuDetailVoJson, SkuDetailVo.class);
            } catch (Exception e) {
                return null;
            }
        }
        return skuDetailVo;
    }

    @Override
    public Boolean checkSkuDetailVoExistenceByBitmap(Long skuId) {
        String skuKey = this.getSkuKeyById(skuId);
        Boolean res = stringRedisTemplate.opsForValue().getBit(skuKey, skuId);
        return res;
    }

    @Override
    public Boolean saveSkuDetailVoCache(SkuDetailVo skuDetailVo) {
        if (!ObjectUtils.isEmpty(skuDetailVo)) {
            // 将skuInfo存入缓存
            Long skuId = skuDetailVo.getSkuInfo().getId();
            String skuKey = this.getSkuKeyById(skuId);
            stringRedisTemplate.opsForValue().set(skuKey, JSON.toJSONString(skuDetailVo));

/*            //更新 bitmap
            stringRedisTemplate.opsForValue().setBit(skuKey, skuId, true);*/
            return true;
        }
        return false;
    }

}
