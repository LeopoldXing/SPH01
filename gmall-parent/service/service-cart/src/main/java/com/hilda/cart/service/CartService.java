package com.hilda.cart.service;

import com.hilda.common.constant.RedisConst;
import com.hilda.common.util.RequestUtil;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

public interface CartService {

    Boolean addCartItem(Long skuId, Integer num);

    List<CartInfo> getCartItemList();

    Boolean checkItem(Long skuId, Integer isChecked);

    Boolean deleteItem(Long skuId);

    Boolean deleteCheckedItems();

    void updateCartItemPrice(String cartKey, List<CartInfo> cartItemList);

    default boolean authenticateCartNum(StringRedisTemplate redisTemplate) {
        Long count = redisTemplate.opsForHash().size(this.generateCartKey());
        return count < 200L;
    }

    default String generateCartKey() {
        String res = RedisConst.CARTKEY_PREFIX + RedisConst.CARTKEY_SUFFIX;
        return res + (RequestUtil.isTemp() ? RequestUtil.getTempUID() : RequestUtil.getUID());
    }

}
