package com.kkwrite.demo.capital.service;

import com.kkwrite.demo.capital.CapitalAccountService;
import com.kkwrite.demo.capital.repository.CapitalAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("capitalAccountService")
public class CapitalAccountServiceImpl implements CapitalAccountService {

    @Autowired
    private CapitalAccountRepository cpitalAccountRepository;

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return cpitalAccountRepository.findByUserId(userId).getBalanceAmount();
    }

}