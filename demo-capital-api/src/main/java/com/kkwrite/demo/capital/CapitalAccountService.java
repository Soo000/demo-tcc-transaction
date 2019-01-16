package com.kkwrite.demo.capital;

import java.math.BigDecimal;

public interface CapitalAccountService {

    /**
     * 根据 userId 获取资金账户金额
     * @param userId
     * @return
     */
    BigDecimal getCapitalAccountByUserId(long userId);

}
