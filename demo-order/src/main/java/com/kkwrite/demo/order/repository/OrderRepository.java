package com.kkwrite.demo.order.repository;

import com.kkwrite.demo.order.dao.OrderDAO;
import com.kkwrite.demo.order.dao.OrderLineDAO;
import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.entity.OrderLineDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderLineDAO orderLineDao;

    public void createOrder(OrderDO orderDO) {
        // 订单入库
        orderDAO.insert(orderDO);
        // 订单项入库
        for (OrderLineDO orderLineDO: orderDO.getOrderLines()) {
            orderLineDao.insert(orderLineDO);
        }
    }

    public int updateOrder(OrderDO orderDO) {
        orderDO.updateVersion();
        int effectCount = orderDAO.update(orderDO);
        if (effectCount < 1) {
            throw new OptimisticLockingFailureException("update order failed");
        }
        return effectCount;
    }

    public OrderDO findByMerchantOrderNo(String merchantOrderNo) {
        return orderDAO.findByMerchantOrderNo(merchantOrderNo);
    }
}
