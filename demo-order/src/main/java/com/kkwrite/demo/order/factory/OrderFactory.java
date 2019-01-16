package com.kkwrite.demo.order.factory;

import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.entity.OrderLineDO;
import com.kkwrite.demo.order.repository.ProductRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderFactory {

    @Autowired
    private ProductRepository productRepository;

    public OrderDO buildOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {
        // 构造一个订单对象
        OrderDO order = new OrderDO(payerUserId, payeeUserId);

        // 给订单中添加商品内容
        for (Pair<Long, Integer> pair: productQuantities) {
            long productId = pair.getLeft();
            BigDecimal price = productRepository.findById(productId).getPrice();
            OrderLineDO orderLineDO = new OrderLineDO(productId, pair.getRight(), price);
            order.addOrderLine(orderLineDO);
        }

        return order;
    }

}
