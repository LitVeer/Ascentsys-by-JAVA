package com.ascent.repository;

import java.util.HashMap;

import com.ascent.bean.Product;

public interface ShoppingCartAccessor {

    // 添加产品
    boolean addProduct(Product product);

    // 删除产品
    boolean removeProduct(String productName);

    // 清空购物车
    boolean clear();

    // 获取购物车中的产品列表
    HashMap<String, Product> getProducts();

    // 获取购物车中的产品总价
    double getTotalPrice();

}
