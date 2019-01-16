package com.kkwrite.demo.capital.controller;

import com.kkwrite.demo.capital.CapitalAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@Controller
public class DemoCapitalControl {

    private Logger logger = LoggerFactory.getLogger(DemoCapitalControl.class);

    @Autowired
    private CapitalAccountService capitalAccountService;

    @RequestMapping("/")
    public String index() {
        return "Demo Capital";
    }

    @RequestMapping("/test")
    public String test(long userId) {
        BigDecimal account = capitalAccountService.getCapitalAccountByUserId(userId);
        if (logger.isDebugEnabled()) {
            logger.debug("用户 {} 的资金为 ￥{}。", userId, account);
        }

        return String.valueOf(account.floatValue());
    }

}
