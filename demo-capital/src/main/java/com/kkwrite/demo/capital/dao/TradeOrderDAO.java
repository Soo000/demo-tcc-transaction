package com.kkwrite.demo.capital.dao;

import com.kkwrite.demo.capital.entity.TradeOrderDO;

public interface TradeOrderDAO {

    int insert(TradeOrderDO tradeOrderDO);

    int update(TradeOrderDO tradeOrderDO);

    TradeOrderDO findByMerchantOrderNo(String merchantOrderNo);

}
