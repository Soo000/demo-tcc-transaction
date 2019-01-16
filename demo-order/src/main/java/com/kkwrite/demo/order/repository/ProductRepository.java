package com.kkwrite.demo.order.repository;

import com.kkwrite.demo.order.dao.ProductDAO;
import com.kkwrite.demo.order.entity.ProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    @Autowired
    ProductDAO productDAO;

    public ProductDO findById(long productId){
        return productDAO.findById(productId);
    }

    public List<ProductDO> findByShopId(long shopId){
        return productDAO.findByShopId(shopId);
    }
}
