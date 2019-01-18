package com.kkwrite.demo.capital.entity;

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

    public void confirm() {
        this.status = "CONFIRM";
    }

    public void cancel() {
        this.status = "CANCEL";
    }

    public long getId() {
        return id;
    }

    public long getSelfUserId() {
        return selfUserId;
    }

    public long getOppositeUserId() {
        return oppositeUserId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version += 1;
    }
}
