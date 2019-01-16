package com.kkwrite.demo.order.dao;

import com.kkwrite.demo.order.entity.ProductDO;

import java.util.List;

public interface ProductDAO {

    ProductDO findById(long productId);

    List<ProductDO> findByShopId(long shopId);
}
