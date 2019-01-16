package com.kkwrite.demo.order.service;

import com.kkwrite.demo.capital.CapitalAccountService;
import com.kkwrite.demo.redpacket.service.RedPacketAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("accountService")
public class AccountService {

    @Autowired
    CapitalAccountService capitalAccountService;

    @Autowired
    RedPacketAccountService redPacketAccountService;

    public BigDecimal getCapitalAccountByUserId(long userId){
        return capitalAccountService.getCapitalAccountByUserId(userId);
    }

    public BigDecimal getRedPacketAccountByUserId(long userId){
        return redPacketAccountService.getRedpacketAccountByUserId(userId);
    }

}
