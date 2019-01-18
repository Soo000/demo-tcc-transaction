package com.kkwrite.demo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderDO implements Serializable {

    private static final long serialVersionUID = -5908730245224893590L;

    private long id;
    private long payerUserId;
    private long payeeUserId;
    private BigDecimal redPacketPayAmount;
    private BigDecimal capitalPayAmount;
    private String status = "DRAFT";
    private String merchantOrderNo;
    private long version = 1L;

    // 商品
    private List<OrderLineDO> orderLines = new ArrayList<>();

    public OrderDO() {
    }

    public OrderDO(long payerUserId, long payeeUserId) {
        this.payerUserId = payerUserId;
        this.payeeUserId = payeeUserId;
        // 贸易编码是一个 uuid
        this.merchantOrderNo = UUID.randomUUID().toString();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLineDO orderLineDO : orderLines) {
            totalAmount = totalAmount.add(orderLineDO.getTotalAmount());
        }
        return totalAmount;
    }

    public void addOrderLine(OrderLineDO orderLineDO) {
        this.orderLines.add(orderLineDO);
    }

    public void pay(BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        this.redPacketPayAmount = redPacketPayAmount;
        this.capitalPayAmount = capitalPayAmount;
        this.status = "PAYING";
    }

    public void confirm() {
        this.status = "CONFIRMED";
    }

    public void cancel() {
        this.status = "PAY_FAILED";
    }

    public void updateVersion() {
        version++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(long payerUserId) {
        this.payerUserId = payerUserId;
    }

    public long getPayeeUserId() {
        return payeeUserId;
    }

    public void setPayeeUserId(long payeeUserId) {
        this.payeeUserId = payeeUserId;
    }

    public BigDecimal getRedPacketPayAmount() {
        return redPacketPayAmount;
    }

    public void setRedPacketPayAmount(BigDecimal redPacketPayAmount) {
        this.redPacketPayAmount = redPacketPayAmount;
    }

    public BigDecimal getCapitalPayAmount() {
        return capitalPayAmount;
    }

    public void setCapitalPayAmount(BigDecimal capitalPayAmount) {
        this.capitalPayAmount = capitalPayAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<OrderLineDO> getOrderLines() {
        return orderLines;
    }

}
