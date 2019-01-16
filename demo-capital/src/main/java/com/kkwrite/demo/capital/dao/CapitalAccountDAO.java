package com.kkwrite.demo.capital.dao;

import com.kkwrite.demo.capital.entity.CapCapitalAccountDO;

public interface CapitalAccountDAO {

    CapCapitalAccountDO findByUserId(long userId);

    int update(CapCapitalAccountDO capitalAccountDO);

}
