package com.ascent.service;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ascent.bean.Product;
import com.ascent.repository.DatabaseConnection;
import com.ascent.repository.ProductDataAccessor;

public class ProductDataAccessorImpl implements ProductDataAccessor {

    private Connection connection = null;

    // 构造函数：初始化数据库连接
    public ProductDataAccessorImpl() {
        connection = DatabaseConnection.getConnection();
    }
    // 添加产品信息
    @Override
    public boolean addProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Products (productname, cas, structurePicPath, formula, price, realstock, category) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, product.getProductname());
            statement.setString(2, product.getCas());
            statement.setString(3, product.getStructurePicPath());
            statement.setString(4, product.getFormula());
            statement.setDouble(5, product.getPrice());
            statement.setString(6, product.getRealstock());
            statement.setString(7, product.getCategory());
            statement.executeUpdate();
            System.out.println(product.getProductname() + "  添加成功");
            return true;
        } catch (SQLException e) {
            System.out.println(product.getProductname() + "  添加失败");
            e.printStackTrace();
            return false;
        }
    }

    // 删除产品信息
    public boolean deleteProduct(String productname) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Products WHERE productname = ?");
            statement.setString(1, productname);
            statement.executeUpdate();
            System.out.println(productname + "  删除成功");
            return true;
        } catch (SQLException e) {
            System.out.println(productname + "  删除失败");
            e.printStackTrace();
            return false;
        }
    }

    // 修改产品信息
    public boolean updateProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Products SET cas = ?, structurePicPath = ?, formula = ?, price = ?, realstock = ?, category = ? WHERE productname = ?");
            statement.setString(1, product.getCas());
            statement.setString(2, product.getStructurePicPath());
            statement.setString(3, product.getFormula());
            statement.setDouble(4, product.getPrice());
            statement.setString(5, product.getRealstock());
            statement.setString(6, product.getCategory());
            statement.setString(7, product.getProductname());
            statement.executeUpdate();
            System.out.println(product.getProductname() + "  修改成功");
            return true;
        } catch (SQLException e) {
            System.out.println(product.getProductname() + "  修改失败");
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有产品信息
    public HashMap<String, Product> getAllProducts() {
        HashMap<String, Product> products = new HashMap<String, Product>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Product product = new Product();
                product.setProductname(result.getString("productname"));
                product.setCas(result.getString("cas"));
                product.setStructurePicPath(result.getString("structurePicPath"));
                product.setFormula(result.getString("formula"));
                product.setPrice(result.getDouble("price"));
                product.setRealstock(result.getString("realstock"));
                product.setCategory(result.getString("category"));
                products.put(product.getProductname(), product);
                System.out.println(product.getProductname() + "  获取成功");
            }
            System.out.println("所有产品信息获取成功");
        } catch (SQLException e) {
            System.out.println("产品信息获取失败");
            e.printStackTrace();
        }
        return products;
    }

    // public static void main(String[] args) {
    //     ProductDataAccessor productDataAccessor = new ProductDataAccessorImpl();
    //     productDataAccessor.getAllProducts();
    //     productDataAccessor.addProduct(new Product("test", "test", "test", "test", 0.0, "test", "test"));
    // }
    
}
