package com.hilda.cart.service;

import com.hilda.common.constant.RedisConst;
import com.hilda.common.util.RequestUtil;
import com.hilda.model.vo.cart.CartInfo;

import java.util.List;

public interface CartService {

    Boolean addCartItem(Long skuId, Integer num);

    List<CartInfo> getCartItem(String uid);

    Boolean checkItem(Long skuId, Integer isChecked);

    Boolean deleteItem(Long skuId);

    default String generateCartKey() {
        String res = RedisConst.CARTKEY_PREFIX + RedisConst.CARTKEY_SUFFIX;
        return res + (RequestUtil.isTemp() ? RequestUtil.getTempUID() : RequestUtil.getUID());
    }

}
