package com.kkwrite.demo.order.repository;

import com.kkwrite.demo.order.dao.ShopDAO;
import com.kkwrite.demo.order.entity.ShopDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShopRepository {

    @Autowired
    private ShopDAO shopDao;

    public ShopDO findById(long shopId) {
        return shopDao.findById(shopId);
    }

}
