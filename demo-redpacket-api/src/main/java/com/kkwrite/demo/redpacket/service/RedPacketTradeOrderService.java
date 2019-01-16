package com.kkwrite.demo.redpacket.service;

import com.kkwrite.demo.redpacket.service.dto.RedPacketTradeOrderDTO;
import org.mengyun.tcctransaction.api.Compensable;

public interface RedPacketTradeOrderService {

    @Compensable
    public String record(RedPacketTradeOrderDTO tradeOrderDTO);

}
