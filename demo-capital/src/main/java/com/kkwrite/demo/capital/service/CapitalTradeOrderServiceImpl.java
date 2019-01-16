package com.kkwrite.demo.capital.service;

import com.kkwrite.demo.capital.CapitalTradeOrderService;
import com.kkwrite.demo.capital.dto.CapitalTradeOrderDTO;
import com.kkwrite.demo.capital.entity.CapCapitalAccountDO;
import com.kkwrite.demo.capital.entity.TradeOrderDO;
import com.kkwrite.demo.capital.repository.CapitalAccountRepository;
import com.kkwrite.demo.capital.repository.TradeOrderRepository;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("capitalTradeOrderService")
public class CapitalTradeOrderServiceImpl implements CapitalTradeOrderService {

    private Logger logger = LoggerFactory.getLogger(CapitalTradeOrderServiceImpl.class);

    @Autowired
    private CapitalAccountRepository capitalAccountRepository;

    @Autowired
    private TradeOrderRepository tradeOrderRepository;

    /**
     * 资金账户 try
     * @param tradeOrderDTO
     * @return
     */
    @Override
    @Compensable(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional
    public String record(CapitalTradeOrderDTO tradeOrderDTO) {
        logger.info("资金账户 try 操作，执行 CapitalTradeOrderServiceImpl.record() 方法");

        // 1.根据贸易编号查询贸易记录
        TradeOrderDO foundTradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        if (foundTradeOrder == null) {
            // 如果没有存在的贸易记录，则新创建一个
            TradeOrderDO tradeOrderDO = new TradeOrderDO(
                    tradeOrderDTO.getSelfUserId(),
                    tradeOrderDTO.getOppositeUserId(),
                    tradeOrderDTO.getMerchantOrderNo(),
                    tradeOrderDTO.getAmount()
            );

            try {
                // 2. 贸易记录入库
                tradeOrderRepository.insert(tradeOrderDO);
                // 付款方人金账户
                CapCapitalAccountDO capitalAccountFrom = capitalAccountRepository.findByUserId(tradeOrderDTO.getSelfUserId());
                capitalAccountFrom.transferFrom(tradeOrderDTO.getAmount());
                // 3. 更新付款人的资金账户
                capitalAccountRepository.update(capitalAccountFrom);
            } catch (DataIntegrityViolationException e) {
                // this exception may happen when insert trade order concurrently, if happened, ignore this insert operation.
            }
        }

        return "success";
    }

    /**
     * 资金账户 confirm
     * @param tradeOrderDTO
     */
    @Transactional
    public void confirmRecord(CapitalTradeOrderDTO tradeOrderDTO) {
        logger.info("资金账户 confirm 操作，执行 CapitalTradeOrderServiceImpl.confirmRecord() 方法");

        // 1. 根据贸易编号查询贸易记录
        TradeOrderDO foundTradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        // 如果贸易记录不为空并且状态是DRAFT
        if (foundTradeOrder != null && "DRAFT".equals(foundTradeOrder.getStatus())) {
            // 设置状态为 “CONFIRM”
            foundTradeOrder.confirm();
            // 2. 更新贸易数据记录状态为 CONFIRM
            tradeOrderRepository.update(foundTradeOrder);
            // 3. 更新收款人的资金账户（增加实际的资金收入）
            CapCapitalAccountDO capitalAccountTO = capitalAccountRepository.findByUserId(tradeOrderDTO.getOppositeUserId());
            capitalAccountTO.transferTo(tradeOrderDTO.getAmount());
            capitalAccountRepository.update(capitalAccountTO);
        }
    }

    /**
     * 资金账户 cancel
     * @param tradeOrderDTO
     */
    @Transactional
    public void cancelRecord(CapitalTradeOrderDTO tradeOrderDTO) {
        logger.info("资金账户 cancel 操作，执行 CapitalTradeOrderServiceImpl.cancelRecord() 方法");

        // 1. 根据贸易编号查询贸易记录
        TradeOrderDO foundTradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        // 如果贸易记录不为空并且状态是DRAFT
        if (foundTradeOrder != null && "DRAFT".equals(foundTradeOrder.getStatus())) {
            // 设置状态为 “CANCEL”
            foundTradeOrder.cancel();
            // 2. 更新贸易数据记录状态为 CANCEL
            tradeOrderRepository.update(foundTradeOrder);
            // 3. 回恢复付款人的资金账户金额
            CapCapitalAccountDO capitalAccountFrom = capitalAccountRepository.findByUserId(tradeOrderDTO.getSelfUserId());
            capitalAccountFrom.cancelTransfer(tradeOrderDTO.getAmount());
            capitalAccountRepository.update(capitalAccountFrom);
        }
    }

}
