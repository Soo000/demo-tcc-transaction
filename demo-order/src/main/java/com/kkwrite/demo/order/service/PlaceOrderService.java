package com.kkwrite.demo.order.service;

import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.entity.ShopDO;
import com.kkwrite.demo.order.repository.ShopRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.mengyun.tcctransaction.CancellingException;
import org.mengyun.tcctransaction.ConfirmingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlaceOrderService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;

    public String placeOrder(long payerUserId, long shopId, List<Pair<Long, Integer>> productQuantities, BigDecimal redpacketPayAmount) {
        // 商店信息
        ShopDO shop = shopRepository.findById(shopId);
        // 生成一个订单，订单数据入库（订单项入库）
        OrderDO orderDO = orderService.createOrder(payerUserId, shop.getOwnerUserId(), productQuantities);

        Boolean result = false;
        try {
            // 支付服务进行支付
            paymentService.makePayment(orderDO, redpacketPayAmount, orderDO.getTotalAmount().subtract(redpacketPayAmount));
        } catch (ConfirmingException confirmingException) {
            //exception throws with the tcc transaction status is CONFIRMING,
            //when tcc transaction is confirming status,
            // the tcc transaction recovery will try to confirm the whole transaction to ensure eventually consistent.
            result = true;
        } catch (CancellingException cancellingException) {
            //exception throws with the tcc transaction status is CANCELLING,
            //when tcc transaction is under CANCELLING status,
            // the tcc transaction recovery will try to cancel the whole transaction to ensure eventually consistent.
        } catch (Throwable e) {
            //other exceptions throws at TRYING stage.
            //you can retry or cancel the operation.
            e.printStackTrace();
        }

        return orderDO.getMerchantOrderNo();
    }

}
