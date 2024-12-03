package com.ascent.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.ascent.repository.DatabaseConnection;
import com.ascent.repository.ShoppingCartAccessor;
import com.ascent.bean.Product;

public class ShoppingCartAccessorImpl implements ShoppingCartAccessor {
    
    private Connection connection = null;
    private HashMap<String, Product> cart = null;
        

    // 构造函数：初始化数据库连接
    public ShoppingCartAccessorImpl() {
        connection = DatabaseConnection.getConnection();
        cart = new HashMap<String, Product>();
    }

    // 添加商品到购物车
    @Override
    public boolean addProduct(Product product) {
        String query = "INSERT INTO ShoppingCart (productName, cas, structurePicPath, formula, price, realstock, category, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            if (cart.containsKey(product.getProductname())) {
                String update = "UPDATE ShoppingCart SET amount = ? WHERE productName = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setInt(1, product.getAmount());
                preparedStatement.setString(2, product.getProductname());
                preparedStatement.executeUpdate();
            } else {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, product.getProductname());
                preparedStatement.setString(2, product.getCas());
                preparedStatement.setString(3, product.getStructurePicPath());
                preparedStatement.setString(4, product.getFormula());
                preparedStatement.setDouble(5, product.getPrice());
                preparedStatement.setString(6, product.getRealstock());
                preparedStatement.setString(7, product.getCategory());
                preparedStatement.setInt(8, product.getAmount() + 1);
                preparedStatement.executeUpdate();
            }
            System.out.println("添加成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("添加失败");
        return false;
    }

    // 删除购物车产品
    @Override
    public boolean removeProduct(String productName) {
        String query = "DELETE FROM ShoppingCart WHERE productName = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productName);
            preparedStatement.executeUpdate();
            System.out.println("删除成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("删除失败");
        return false;
    }

    // 清空购物车
    public boolean clear() {
        String query = "DELETE FROM ShoppingCart";
        try {
            cart.clear();
            connection.createStatement().executeUpdate(query);
            System.out.println("清空购物车成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("清空购物车失败");
        return false;
    }

    // 获取购物车中的商品列表
    public HashMap<String, Product> getProducts() {
        String query = "SELECT * FROM ShoppingCart";
        ResultSet resultSet = null;
        Product product = null;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                product = new Product();
                product.setProductname(resultSet.getString("productName"));
                product.setStructurePicPath(resultSet.getString("structurePicPath"));
                product.setCas(resultSet.getString("cas"));
                product.setFormula(resultSet.getString("formula"));
                product.setPrice(resultSet.getDouble("price"));
                product.setRealstock(resultSet.getString("realstock"));
                product.setCategory(resultSet.getString("category"));
                product.setAmount(resultSet.getInt("amount"));
                cart.put(product.getProductname(), product);
            }
            System.out.println("已获取购物车商品列表！");
            return cart;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println("获取购物车商品列表失败");
        return null;
    }

    // 获取购物车中的总价
    public double getTotalPrice() {
        String query = "SELECT * FROM ShoppingCart";
        ResultSet resultSet = null;
        double totalPrice = 0.0;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                totalPrice += resultSet.getDouble("price") * resultSet.getInt("amount");
            }
            System.out.println("已计算总价！" );
            return totalPrice;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println("计算总价失败");
        return 0.0;
    }

    // test
    // public static void main(String[] args) {
    //     ShoppingCartAccessorImpl cartAccessor = new ShoppingCartAccessorImpl();
    //     cartAccessor.addProduct(new Product("1", null, null, null, 10, null, null, 1));
    //     cartAccessor.addProduct(new Product("2", null, null, null, 20, null, null, 2));
    //     cartAccessor.addProduct(new Product("3", null, null, null, 30, null, null, 3));
    //     cartAccessor.getProducts();
    //     cartAccessor.getTotalPrice();
    //     System.out.println(cartAccessor.getTotalPrice());
    //     cartAccessor.clear();
    // }
}
