package com.kkwrite.demo.redpacket.entity;

import com.kkwrite.demo.common.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class RedPacketAccountDO {

    private long id;
    private BigDecimal balanceAmount;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 红包账号红包额检索
     * @param amount
     */
    public void transferFrom(BigDecimal amount) {
        this.balanceAmount = this.balanceAmount.subtract(amount);
        if (BigDecimal.ZERO.compareTo(this.balanceAmount) > 0) {
            throw new InsufficientBalanceException();
        }
    }

    public void transferTo(BigDecimal amount) {
        this.balanceAmount = this.balanceAmount.add(amount);
    }

    public void cancelTransfer(BigDecimal amount) {
        transferTo(amount);
    }
}
