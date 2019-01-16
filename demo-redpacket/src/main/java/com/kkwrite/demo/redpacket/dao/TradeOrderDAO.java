package com.kkwrite.demo.redpacket.dao;

import com.kkwrite.demo.redpacket.entity.TradeOrderDO;

public interface TradeOrderDAO {

    int insert(TradeOrderDO tradeOrderDO);

    int update(TradeOrderDO tradeOrderDO);

    TradeOrderDO findByMerchantOrderNo(String merchantOrderNo);

}
