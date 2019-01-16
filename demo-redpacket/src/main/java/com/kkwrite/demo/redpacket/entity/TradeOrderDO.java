package com.kkwrite.demo.redpacket.entity;

import java.math.BigDecimal;

public class TradeOrderDO {

    private long id;
    private long selfUserId;
    private long oppositeUserId;
    private String merchantOrderNo;
    private BigDecimal amount;
    private String status = "DRAFT";
    private long version = 1L;

    public TradeOrderDO() {
    }

    public TradeOrderDO(long selfUserId, long oppositeUserId, String merchantOrderNo, BigDecimal amount) {
        this.selfUserId = selfUserId;
        this.oppositeUserId = oppositeUserId;
        this.merchantOrderNo = merchantOrderNo;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void updateVersion() {
        this.version += 1;
    }

    public void confirm() {
        this.status = "CONFIRM";
    }

    public void cancel() {
        this.status = "CANCEL";
    }
}
