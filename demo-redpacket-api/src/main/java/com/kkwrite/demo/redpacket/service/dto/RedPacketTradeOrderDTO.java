package com.kkwrite.demo.redpacket.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RedPacketTradeOrderDTO implements Serializable {

    private static final long serialVersionUID = -4760592958369069673L;

    private long selfUserId;
    private long oppositeUserId;
    private String merchantOrderNo;
    private BigDecimal amount;
    private String orderTitle;

    public long getSelfUserId() {
        return selfUserId;
    }

    public void setSelfUserId(long selfUserId) {
        this.selfUserId = selfUserId;
    }

    public long getOppositeUserId() {
        return oppositeUserId;
    }

    public void setOppositeUserId(long oppositeUserId) {
        this.oppositeUserId = oppositeUserId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }
}
