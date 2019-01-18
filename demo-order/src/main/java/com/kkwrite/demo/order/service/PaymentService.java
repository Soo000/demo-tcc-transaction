package com.kkwrite.demo.order.service;

import com.kkwrite.demo.capital.CapitalTradeOrderService;
import com.kkwrite.demo.capital.dto.CapitalTradeOrderDTO;
import com.kkwrite.demo.order.entity.OrderDO;
import com.kkwrite.demo.order.repository.OrderRepository;
import com.kkwrite.demo.redpacket.service.RedPacketTradeOrderService;
import com.kkwrite.demo.redpacket.service.dto.RedPacketTradeOrderDTO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 支付服务
 */
@Service
public class PaymentService {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CapitalTradeOrderService capitalTradeOrderService;

    @Autowired
    private RedPacketTradeOrderService redPacketTradeOrderService;

    /**
     * 支付 try，进行 tcc 事务控制
     * @param orderDO
     * @param redPacketPayAmount
     * @param capitalPayAmount
     */
    @Compensable(confirmMethod = "confirmMakePayment", cancelMethod = "cancelMakePayment", asyncConfirm = true)
    public void makePayment(OrderDO orderDO, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        String operTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行了订单支付服务 try 操作 " + operTime);
        // 1. 判断订单状态是不是 "DRAFT"，如果不是，意味着该订单的支付方法被被别的地方调用了，忽略本次调用
        if ("DRAFT".equals(orderDO.getStatus())) {
            // 设置订单中红包支付金额，资金支付金额，订单状态为 "PAYING"
            orderDO.pay(redPacketPayAmount, capitalPayAmount);

            try {
                // 更新订单数据
                orderRepository.updateOrder(orderDO);
            } catch (OptimisticLockingFailureException e) {
                //ignore the concurrently update order exception, ensure idempotency.
            }
        }
        // 资金 资金执行了confirm方法
        String capitalPayResult = capitalTradeOrderService.record(buildCapitalTradeOrderDTO(orderDO));
        // 红包 红包执行了cancel 方法
        String redPacketPayResult = redPacketTradeOrderService.record(buildRedPacketTradeOrderDTO(orderDO));
    }

    /**
     * 支付 confirm
     * @param orderDO
     * @param redPacketPayAmount
     * @param capitalPayAmount
     */
    public void confirmMakePayment(OrderDO orderDO, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        String operTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行了订单支付服务 confrim 操作 " + operTime);

        // 根据 贸易编号 查找订单
        OrderDO founderOrderDO = orderRepository.findByMerchantOrderNo(orderDO.getMerchantOrderNo());
        // 如果订单状态是正在支付 PAYING，如果不是意味着其它地方已经调用了 confirm 方法，直接返回
        if (founderOrderDO != null && "PAYING".equals(founderOrderDO.getStatus())) {
            // 更改订单的状态为 "CONFIRM"
            orderDO.confirm();
            orderRepository.updateOrder(orderDO);

            // TODO 在这个地方为什么没有调用 红包账号和资金账户的 confirm 方法呢？
        }
    }

    /**
     * 支付 cancel
     * @param orderDO
     * @param redPacketPayAmount
     * @param capitalPayAmount
     */
    public void cancelMakePayment(OrderDO orderDO, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        String operTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行了订单支付服务 cancel 操作 " + operTime);

        // 根据 贸易编号 查找订单
        OrderDO founderOrderDO = orderRepository.findByMerchantOrderNo(orderDO.getMerchantOrderNo());
        // 如果订单状态是正在支付 PAYING，如果不是意味着其它地方已经调用了 cancel 方法，直接返回
        if (founderOrderDO != null && "PAYING".equals(founderOrderDO.getStatus())) {
            orderDO.cancel();
            orderRepository.updateOrder(orderDO);

            // TODO 在这个地方为什么没有调用 红包账号和资金账户的 cancel 方法呢？
        }
    }

    private CapitalTradeOrderDTO buildCapitalTradeOrderDTO(OrderDO orderDO) {
        CapitalTradeOrderDTO tradeOrderDTO = new CapitalTradeOrderDTO();
        tradeOrderDTO.setAmount(orderDO.getCapitalPayAmount());
        tradeOrderDTO.setMerchantOrderNo(orderDO.getMerchantOrderNo());
        tradeOrderDTO.setSelfUserId(orderDO.getPayerUserId());
        tradeOrderDTO.setOppositeUserId(orderDO.getPayeeUserId());

        tradeOrderDTO.setOrderTitle(String.format("order no:%s", orderDO.getMerchantOrderNo()));

        return tradeOrderDTO;
    }

    private RedPacketTradeOrderDTO buildRedPacketTradeOrderDTO(OrderDO orderDO) {
        RedPacketTradeOrderDTO tradeOrderDTO = new RedPacketTradeOrderDTO();
        tradeOrderDTO.setAmount(orderDO.getRedPacketPayAmount());
        tradeOrderDTO.setMerchantOrderNo(orderDO.getMerchantOrderNo());
        tradeOrderDTO.setSelfUserId(orderDO.getPayerUserId());
        tradeOrderDTO.setOppositeUserId(orderDO.getPayeeUserId());
        tradeOrderDTO.setOrderTitle(String.format("order no:%s", orderDO.getMerchantOrderNo()));
        return tradeOrderDTO;
    }

}
