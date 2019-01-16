package com.kkwrite.demo.order.dao;

import com.kkwrite.demo.order.entity.OrderDO;

public interface OrderDAO {

    public int insert(OrderDO orderDO);

    public int update(OrderDO orderDO);

    OrderDO findByMerchantOrderNo(String merchantOrderNo);

}
