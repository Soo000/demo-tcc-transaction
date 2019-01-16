package com.kkwrite.demo.redpacket.dao;

import com.kkwrite.demo.redpacket.entity.RedPacketAccountDO;

public interface RedPacketAccountDAO {

    RedPacketAccountDO findByUserId(long userId);

    int update(RedPacketAccountDO redPacketAccountDO);

}
