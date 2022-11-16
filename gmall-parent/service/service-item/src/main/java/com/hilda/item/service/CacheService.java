package com.hilda.item.service;

import com.hilda.common.constant.RedisConst;
import com.hilda.model.vo.item.SkuDetailVo;

public interface CacheService {

    SkuDetailVo getSkuDetailVoFromCache(Long skuId);

    Boolean checkSkuDetailVoExistenceByBitmap(Long skuId);

    Boolean saveSkuDetailVoCache(SkuDetailVo skuDetailVo);

    default String getSkuKeyById(Long skuId) {
        return RedisConst.SKUKEY_PREFIX + RedisConst.SKUKEY_SUFFIX + skuId;
    }

}
