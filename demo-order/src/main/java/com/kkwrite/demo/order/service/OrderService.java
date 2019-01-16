package com.kkwrite.demo.order.service;

import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.factory.OrderFactory;
import com.kkwrite.demo.order.repository.OrderRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrderDO createOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {
        // 通过订单工厂生成一个订单
        OrderDO orderDO = orderFactory.buildOrder(payerUserId, payeeUserId, productQuantities);
        // 订单入库
        orderRepository.createOrder(orderDO);

        return orderDO;
    }

    public OrderDO findOrderByMerchantOrderNo(String merchantOrderNo) {
        return orderRepository.findByMerchantOrderNo(merchantOrderNo);
    }

}
