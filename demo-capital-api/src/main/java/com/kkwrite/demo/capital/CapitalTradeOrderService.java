package com.kkwrite.demo.capital;

import com.kkwrite.demo.capital.dto.CapitalTradeOrderDTO;
import org.mengyun.tcctransaction.api.Compensable;

public interface CapitalTradeOrderService {

    @Compensable
    public String record(CapitalTradeOrderDTO tradeOrderDTO);

}
