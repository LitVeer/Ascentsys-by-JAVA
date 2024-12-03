package com.ascent.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.ascent.repository.ShoppingCartAccessor;
import com.ascent.bean.Product;

public class ShoppingCartAccessorImpl implements ShoppingCartAccessor {

    private HashMap<Product, Integer> cart = null;
    private double totalPrice;

    // 构造函数：初始化购物车
    public ShoppingCartAccessorImpl() {
        cart = new HashMap<Product, Integer>();
        totalPrice = 0.0;
    }

    // 添加商品到购物车
    @Override
    public void addProduct(Product product) {
        try {
            if (cart.containsKey((Object) product.getProductname())) {
                cart.put(product, cart.get(product) + 1);
            } else {
                cart.put(product, 1); // 第一次添加该商品，数量为1
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除购物车产品
    @Override
    public void removeProduct(Product product) {
        try {
            if (cart.containsKey((Object) product.getProductname())) {
                cart.remove(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清空购物车
    @Override
    public void clear() {
        cart.clear();
        totalPrice = 0.0;
    }

    // 获取购物车中的商品列表
    @Override
    public ArrayList<Product> getProducts() {
        return new ArrayList<Product>(cart.keySet());
    }

    // 计算购物车中的商品总价
    @Override
    public double getTotalPrice() {
        for (Product product : cart.keySet()) {
            totalPrice += product.getPrice() * cart.get(product);
        }
        return totalPrice;
    }

}
