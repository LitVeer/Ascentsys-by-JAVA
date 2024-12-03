package com.ascent.repository;

import java.util.HashMap;

import com.ascent.bean.Product;

public interface ProductDataAccessor {
    
    // 添加产品信息
    public boolean addProduct(Product product);

    // 删除产品信息
    public boolean deleteProduct(String productname);

    // 修改产品信息
    public boolean updateProduct(Product product);

    // 获取所有产品信息
    public HashMap<String, Product> getAllProducts();
    
}
