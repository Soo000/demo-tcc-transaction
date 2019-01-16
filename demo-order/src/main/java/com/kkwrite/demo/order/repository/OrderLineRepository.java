package com.kkwrite.demo.order.repository;

import com.kkwrite.demo.order.dao.OrderLineDAO;
import com.kkwrite.demo.order.entity.OrderLineDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineRepository {

    @Autowired
    private OrderLineDAO orderLineDao;

    int insert(OrderLineDO orderLineDO) {
        return orderLineDao.insert(orderLineDO);
    }

}
