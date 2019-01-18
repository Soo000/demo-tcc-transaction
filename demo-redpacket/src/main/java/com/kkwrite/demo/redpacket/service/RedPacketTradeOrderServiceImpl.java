package com.kkwrite.demo.redpacket.service;

import com.kkwrite.demo.redpacket.entity.RedPacketAccountDO;
import com.kkwrite.demo.redpacket.entity.TradeOrderDO;
import com.kkwrite.demo.redpacket.repository.RedPacketAccountRepository;
import com.kkwrite.demo.redpacket.repository.TraderOrderRepository;
import com.kkwrite.demo.redpacket.service.dto.RedPacketTradeOrderDTO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service("redPacketTradeOrderService")
public class RedPacketTradeOrderServiceImpl implements RedPacketTradeOrderService {

    private Logger logger = LoggerFactory.getLogger(RedPacketTradeOrderServiceImpl.class);

    @Autowired
    private TraderOrderRepository traderOrderRepository;

    @Autowired
    private RedPacketAccountRepository redPacketAccountRepository;

    /**
     * 红包账户 try
     * @param tradeOrderDTO
     * @return
     */
    @Override
    @Compensable(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional
    public String record(RedPacketTradeOrderDTO tradeOrderDTO) {
        String callTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行红包账户 try 操作 " + callTime);

        // 1. 查询贸易数据
        TradeOrderDO foundTradeOrder = traderOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        if (foundTradeOrder == null) {
            // 如果贸易数据为空，则新创建一个
            TradeOrderDO tradeOrderDO = new TradeOrderDO(
                    tradeOrderDTO.getSelfUserId(),
                    tradeOrderDTO.getOppositeUserId(),
                    tradeOrderDTO.getMerchantOrderNo(),
                    tradeOrderDTO.getAmount()
            );

            try {
                // 2. 插入贸易数据
                traderOrderRepository.insert(tradeOrderDO);
                // 3. 付款人红包账户减去付款红包金额
                RedPacketAccountDO redPacketAccountFrom = redPacketAccountRepository.findByUserId(tradeOrderDO.getSelfUserId());
                redPacketAccountFrom.transferFrom(tradeOrderDTO.getAmount());
                redPacketAccountRepository.update(redPacketAccountFrom);
            } catch (DataIntegrityViolationException e) {
                //this exception may happen when insert trade order concurrently, if happened, ignore this insert operation.
            }
        }

        return "success";
    }

    /**
     * 红包账户 confirm
     * @param tradeOrderDTO
     */
    @Transactional
    public void confirmRecord(RedPacketTradeOrderDTO tradeOrderDTO) {
        String callTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行红包账户 confrim 操作 " + callTime);

        // 1. 查询贸易数据
        TradeOrderDO foundTradeOrder = traderOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        // 判断如果红包贸易数据存在且是 “DRAFT”
        if (foundTradeOrder != null &&
                "DRAFT".equals(foundTradeOrder.getStatus())) {
            // 2. 修改贸易数据的状态值为 "CONFIRM"
            foundTradeOrder.confirm();
            traderOrderRepository.update(foundTradeOrder);
            // 3. 收款方红包额增加红包数
            RedPacketAccountDO redpacketAccountTo = redPacketAccountRepository.findByUserId(tradeOrderDTO.getOppositeUserId());
            redpacketAccountTo.transferTo(tradeOrderDTO.getAmount());
            redPacketAccountRepository.update(redpacketAccountTo);
        }
    }

    /**
     * 红包账户 cancel
     * @param tradeOrderDTO
     */
    @Transactional
    public void cancelRecord(RedPacketTradeOrderDTO tradeOrderDTO) {
        String callTime = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("进行红包账户 cancel 操作 " + callTime);

        // 1. 查询贸易数据
        TradeOrderDO foundTradeOrder = traderOrderRepository.findByMerchantOrderNo(tradeOrderDTO.getMerchantOrderNo());
        // 判断如果红包贸易数据存在且是 “DRAFT”
        if (foundTradeOrder != null && "DRAFT".equals(foundTradeOrder.getStatus())) {
            // 2. 修改贸易数据的状态值为 "CANCEL"
            foundTradeOrder.cancel();
            traderOrderRepository.update(foundTradeOrder);
            // 3. 恢复付款方红包额红包数
            RedPacketAccountDO redpacketAccountFrom = redPacketAccountRepository.findByUserId(tradeOrderDTO.getSelfUserId());
            redpacketAccountFrom.cancelTransfer(tradeOrderDTO.getAmount());
            redPacketAccountRepository.update(redpacketAccountFrom);
        }
    }

}
