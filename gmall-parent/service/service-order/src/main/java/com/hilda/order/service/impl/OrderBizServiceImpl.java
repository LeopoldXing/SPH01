package com.hilda.order.service.impl;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.hilda.common.execption.GmallException;
import com.hilda.common.result.Result;
import com.hilda.common.util.RequestUtil;
import com.hilda.feign.CartFeignClient;
import com.hilda.feign.ProductFeignClient;
import com.hilda.feign.UserFeignClient;
import com.hilda.model.bean.order.OrderDetail;
import com.hilda.model.bean.user.UserAddress;
import com.hilda.model.vo.cart.CartInfo;
import com.hilda.model.vo.order.OrderConfirmPageVo;
import com.hilda.order.mapper.OrderInfoMapper;
import com.hilda.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public OrderConfirmPageVo getOrderConfirmPageData() {
        OrderConfirmPageVo res = new OrderConfirmPageVo();

        // 1. 查询购物车中选中的商品详情
        List<CartInfo> checkedItemList = null;
        Result<List<CartInfo>> checkedItemInfoListResult = cartFeignClient.getCheckedItemInfoList();
        if (!ObjectUtils.isEmpty(checkedItemInfoListResult)) checkedItemList = checkedItemInfoListResult.getData();
        else throw new GmallException("查询购物车中商品错误", 5900);
        List<OrderDetail> orderDetailList = checkedItemList.stream().map(checkedItem -> {
            if (!ObjectUtils.isEmpty(checkedItem)) {
                Integer skuNum = checkedItem.getSkuNum();
                Long skuId = checkedItem.getSkuId();
                BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setSkuId(skuId);
                orderDetail.setSkuName(checkedItem.getSkuName());
                orderDetail.setImgUrl(checkedItem.getImgUrl());
                orderDetail.setOrderPrice(skuPrice.multiply(new BigDecimal(skuNum)));
                orderDetail.setSkuNum(skuNum);
                orderDetail.setCreateTime(new Date());
                return orderDetail;
            } else return null;
        }).filter(orderDetail -> !ObjectUtils.isEmpty(orderDetail)).collect(Collectors.toList());

        // 2. 计算订单总价
        BigDecimal totalAmount = new BigDecimal(0);
        Optional<BigDecimal> totalAmountOptional = orderDetailList.stream().map(OrderDetail::getOrderPrice).reduce(BigDecimal::add);
        if (totalAmountOptional.isPresent()) totalAmount = totalAmountOptional.get();

        // 3. 计算订单商品总件数
        Integer totalNum = 0;
        Optional<Integer> totalNumOptional = orderDetailList.stream().map(OrderDetail::getSkuNum).reduce(Integer::sum);
        if (totalNumOptional.isPresent()) totalNum = totalNumOptional.get();

        // 4. 获取用户收货地址
        List<UserAddress> userAddressList = new ArrayList<>();
        Result<List<UserAddress>> userAddressResult = userFeignClient.getUserAddress();
        if (!ObjectUtils.isEmpty(userAddressResult)) userAddressList = userAddressResult.getData();

        // 5. 生成流水号
        Long tradeNo = System.currentTimeMillis();

        // 生成 订单页面数据VO
        res.setDetailArrayList(orderDetailList);
        res.setTotalNum(totalNum);
        res.setTotalAmount(totalAmount);
        res.setUserAddressList(userAddressList);
        res.setTradeNo(tradeNo);
        res.setId(RequestUtil.getUID());

        return res;
    }
}
