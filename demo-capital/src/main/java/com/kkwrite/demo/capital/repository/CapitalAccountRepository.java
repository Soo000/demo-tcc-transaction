package com.kkwrite.demo.capital.repository;

import com.kkwrite.demo.capital.dao.CapitalAccountDAO;
import com.kkwrite.demo.capital.entity.CapCapitalAccountDO;
import com.kkwrite.demo.common.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CapitalAccountRepository {

    @Autowired
    CapitalAccountDAO capitalAccountDAO;

    public CapCapitalAccountDO findByUserId(long userId) {
        return capitalAccountDAO.findByUserId(userId);
    }

    public void update(CapCapitalAccountDO capitalAccountDO) {
        int effectCount = capitalAccountDAO.update(capitalAccountDO);
        if (effectCount < 1) {
            throw new InsufficientBalanceException();
        }
    }

}
