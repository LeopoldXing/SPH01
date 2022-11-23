package com.hilda.order.service.impl;

import com.hilda.order.mapper.OrderInfoMapper;
import com.hilda.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

}
