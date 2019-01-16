package com.kkwrite.demo.capital.entity;

import com.kkwrite.demo.common.exception.InsufficientBalanceException;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

public class CapCapitalAccountDO {

    private long id;
    private long userId;
    private BigDecimal balanceAmount;
    private BigDecimal transferAmount = BigDecimal.ZERO;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    /**
     * 资金账户金额减少指定的金额
     * @param amount
     */
    public void transferFrom(BigDecimal amount) {
        // 当前资金额 减去 要付款的额度
        this.balanceAmount = this.balanceAmount.subtract(amount);

        // 如果当前的资金额度不够，则抛出异常
        if (BigDecimal.ZERO.compareTo(this.balanceAmount) > 0) {
            throw new InsufficientBalanceException();
        }

        // 付款额度值（为负数）
        this.transferAmount = this.transferAmount.add(amount.negate());
    }

    /**
     * 资金账户金额增加指定的金额
     * @param amount
     */
    public void transferTo(BigDecimal amount) {
        // 当前的资金 加上 收到的额度
        this.balanceAmount = this.balanceAmount.add(amount);

        // 转移额度值
        transferAmount = transferAmount.add(amount);
    }

    public void cancelTransfer(BigDecimal amount) {
        transferTo(amount);
    }
}
