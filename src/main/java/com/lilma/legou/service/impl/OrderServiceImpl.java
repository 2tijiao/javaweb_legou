package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.OrderMapper;
import com.lilma.legou.pojo.Order;
import com.lilma.legou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order[] getOrder(Integer userid) {
        return orderMapper.getOrder(userid);
    }

    @Override
    public void changeState(Integer id) {
        orderMapper.changeState(id);
    }

    @Override
    public void del(Integer id) {
        orderMapper.del(id);
    }
}
