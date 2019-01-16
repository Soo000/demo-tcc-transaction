package com.kkwrite.demo.redpacket.repository;

import com.kkwrite.demo.common.exception.InsufficientBalanceException;
import com.kkwrite.demo.redpacket.dao.RedPacketAccountDAO;
import com.kkwrite.demo.redpacket.entity.RedPacketAccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RedPacketAccountRepository {

    @Autowired
    private RedPacketAccountDAO redPacketAccountDAO;

    public RedPacketAccountDO findByUserId(long userId) {
        return redPacketAccountDAO.findByUserId(userId);
    }

    public int update(RedPacketAccountDO redPacketAccountDO) {
        int effect = redPacketAccountDAO.update(redPacketAccountDO);
        if (effect < 1) {
            throw new InsufficientBalanceException();
        }
        return effect;
    }

}
