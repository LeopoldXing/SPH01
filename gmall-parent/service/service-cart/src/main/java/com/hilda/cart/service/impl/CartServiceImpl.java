package com.hilda.cart.service.impl;

import com.alibaba.fastjson.JSON;

import com.hilda.cart.mapper.CartInfoMapper;
import com.hilda.cart.service.CartService;
import com.hilda.common.constant.RedisConst;
import com.hilda.common.util.RequestUtil;
import com.hilda.feign.ProductFeignClient;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.vo.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public List<CartInfo> getCartItemList() {
        Boolean temp = RequestUtil.isTemp();
        if(!temp) {
            // 当前是登录状态
            String tempUID = RequestUtil.getTempUID();
            if (!StringUtils.isEmpty(tempUID)) {
                String tempKey = RedisConst.CARTKEY_PREFIX + RedisConst.CARTKEY_SUFFIX + tempUID;
                List<Object> tempItemList = redisTemplate.opsForHash().values(tempKey);
                if (!CollectionUtils.isEmpty(tempItemList)) {
                    // 临时购物车中有东西
                    tempItemList.parallelStream().forEach(item -> {
                        if (!ObjectUtils.isEmpty(item)) {
                            String cartInfoJSON = String.valueOf(item);
                            CartInfo cartInfo = JSON.parseObject(cartInfoJSON, CartInfo.class);
                            this.addCartItem(cartInfo.getSkuId(), cartInfo.getSkuNum());
                        }
                    });
                    // 删除临时购物车中的商品
                    redisTemplate.delete(tempKey);
                }
            }
        }

        List<Object> values = redisTemplate.opsForHash().values(this.generateCartKey());
        List<CartInfo> cartInfoList = values.parallelStream().map(value -> JSON.parseObject(String.valueOf(value), CartInfo.class))
                .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                .collect(Collectors.toList());

        return cartInfoList;
    }

    @Override
    public Boolean addCartItem(Long skuId, Integer skuNum) {
        boolean res = false;
        Long uid = RequestUtil.getUID();
        String tempUID = RequestUtil.getTempUID();
        Boolean isTemp = RequestUtil.isTemp();

        if (skuId == null || skuId == 0 || skuNum == null || skuNum == 0) return res;

        String cartKey = this.generateCartKey();
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey, String.valueOf(skuId));

        if (hasKey) {
            // 已经有该商品
            String cartItemJSON = String.valueOf(redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)));
            if (!StringUtils.isEmpty(cartItemJSON) && !cartItemJSON.equalsIgnoreCase("null")) {
                CartInfo cartInfo = JSON.parseObject(cartItemJSON, CartInfo.class);
                cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
                cartInfo.setCartPrice(productFeignClient.getSkuPrice(skuId));
                cartInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));

                redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));
                return true;
            } else return false;
        } else {
            SkuInfo skuInfo = productFeignClient.getSkuInfoById(skuId);
            if (ObjectUtils.isEmpty(skuInfo)) return false;
            // 需要添加新商品
            CartInfo cartInfo = new CartInfo();
            cartInfo.setUserId(String.valueOf(uid));
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            cartInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            cartInfo.setSkuPrice(skuInfo.getPrice());

            if(isTemp) {
                // 临时购物车
                System.out.println("临时");
            } else {
                // 登录的购物车
                System.out.println("已登录");
            }

            redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));

            res = cartInfoMapper.insert(cartInfo) > 0;
        }

        return res;
    }

    @Override
    public Boolean checkItem(Long skuId, Integer isChecked) {
        String cartKey = this.generateCartKey();
        String cartInfoJSON = String.valueOf(redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)));

        if (!StringUtils.isEmpty(cartInfoJSON) && !cartInfoJSON.equalsIgnoreCase("null")) {
            CartInfo cartInfo = JSON.parseObject(cartInfoJSON, CartInfo.class);
            cartInfo.setIsChecked(isChecked);
            try{
                redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public Boolean deleteItem(Long skuId) {
        String cartKey = this.generateCartKey();

        redisTemplate.opsForHash().delete(cartKey, String.valueOf(skuId));

        return true;
    }

    @Override
    public Boolean deleteCheckedItems() {
        List<CartInfo> cartItemList = this.getCartItemList();

        List<String> checkedItemSkuIdList = cartItemList.parallelStream().filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> String.valueOf(cartInfo.getSkuId()))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(checkedItemSkuIdList))
            redisTemplate.opsForHash().delete(generateCartKey(), checkedItemSkuIdList.toArray());

        return true;
    }

}
