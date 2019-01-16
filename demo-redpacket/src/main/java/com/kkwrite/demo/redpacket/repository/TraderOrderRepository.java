package com.kkwrite.demo.redpacket.repository;

import com.kkwrite.demo.redpacket.dao.TradeOrderDAO;
import com.kkwrite.demo.redpacket.entity.TradeOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class TraderOrderRepository {

    @Autowired
    private TradeOrderDAO tradeOrderDAO;

    public void insert(TradeOrderDO tradeOrderDO) {
        tradeOrderDAO.insert(tradeOrderDO);
    }

    public void update(TradeOrderDO tradeOrderDO) {
        tradeOrderDO.updateVersion();
        int effect = tradeOrderDAO.update(tradeOrderDO);
        if (effect < 1) {
            throw new OptimisticLockingFailureException("更新红包贸易记录失败");
        }
    }

    public TradeOrderDO findByMerchantOrderNo(String merchantOrderNo) {
        return tradeOrderDAO.findByMerchantOrderNo(merchantOrderNo);
    }

}
