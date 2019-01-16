package com.kkwrite.demo.redpacket.service;

import com.kkwrite.demo.redpacket.dao.RedPacketAccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("redPacketAccountService")
public class RedPacketAccountServiceImpl implements RedPacketAccountService {

    @Autowired
    private RedPacketAccountDAO redPacketAccountDAO;

    @Override
    public BigDecimal getRedpacketAccountByUserId(long userId) {
        return redPacketAccountDAO.findByUserId(userId).getBalanceAmount();
    }

}
