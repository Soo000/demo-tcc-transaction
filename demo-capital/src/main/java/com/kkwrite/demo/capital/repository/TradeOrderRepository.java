package com.kkwrite.demo.capital.repository;

import com.kkwrite.demo.capital.CapitalAccountService;
import com.kkwrite.demo.capital.dao.TradeOrderDAO;
import com.kkwrite.demo.capital.entity.TradeOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class TradeOrderRepository {

    @Autowired
    private TradeOrderDAO tradeOrderDAO;

    public void insert(TradeOrderDO tradeOrderDO) {
        tradeOrderDAO.insert(tradeOrderDO);
    }

    public void update(TradeOrderDO tradeOrderDO) {
        tradeOrderDO.updateVersion();
        int effectCount = tradeOrderDAO.update(tradeOrderDO);
        if (effectCount < 1) {
            throw new OptimisticLockingFailureException("更新贸易记录失败");
        }
    }

    public TradeOrderDO findByMerchantOrderNo(String merchantOrderNo) {
        return tradeOrderDAO.findByMerchantOrderNo(merchantOrderNo);
    }

}
