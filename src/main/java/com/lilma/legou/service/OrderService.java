package com.lilma.legou.service;

import com.lilma.legou.pojo.Order;

public interface OrderService {
    Order[] getOrder(Integer userid);

    void changeState(Integer id);

    void del(Integer id);
}
