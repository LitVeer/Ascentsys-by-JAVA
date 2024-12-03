package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.ascent.bean.Product;
import com.ascent.service.ProductDataAccessorImpl;

public class ProductManagement extends JFrame {
    private JTable productTable;
    private JButton addProductButton;
    private JButton delProductButton;
    private ProductDataAccessorImpl productDataAccessor = new ProductDataAccessorImpl();
    

    public ProductManagement() {
        setTitle("产品管理面板");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeWindow();
            }
        });
        setLayout(new BorderLayout());

        initializeproductTable();
        initializeButtons();
    }

    private void initializeproductTable() {
        String[] columnNames = {"Product Name", "CAS", "Structure Pic Path", "Forimalula", "Price", "Stock", "Category"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(model);
        productTable.setFillsViewportHeight(true);

        // Add TableModelListener to detect changes in the table
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    updateProductInfo(row);
                }
            }
        });

        updateproductTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);
    }

    private void initializeButtons() {

        addProductButton = new JButton("添加产品");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddProductEvent();
            }
        });

        delProductButton = new JButton("删除产品");
        delProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelProductEvent();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // 添加水平间隔
        buttonPanel.add(delProductButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleAddProductEvent() {
        String Productname = JOptionPane.showInputDialog(this, "请输入产品名称:");
        if (Productname != null && !Productname.isEmpty()) {
            Product product = new Product();
            product.setProductname(Productname);
            productDataAccessor.addProduct(product);
            updateproductTable();
        }
    }

    private void handleDelProductEvent() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product Product = getProductByRow(selectedRow);
            if (Product != null) {
                productDataAccessor.deleteProduct(Product.getProductname());
                updateproductTable();
            }
        }
    }

    private void updateproductTable() {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        ArrayList<Product> products = new ArrayList<>(productDataAccessor.getAllProducts().values());

        for (Product product : products) {
            model.addRow(new Object[] {
                product.getProductname(),
                product.getCas(),
                product.getStructurePicPath(),
                product.getFormula(),
                product.getRealstock(),
                product.getPrice(),
                product.getCategory()
            });
        }
    }

    private void updateProductInfo(int row) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        Object ProductnameObj = model.getValueAt(row, 0);
        Object casObject = model.getValueAt(row, 1);
        Object structurePicPathObj = model.getValueAt(row, 2);
        Object formulaObj = model.getValueAt(row, 3);
        Object realstockObj = model.getValueAt(row, 4);
        Object priceObj = model.getValueAt(row, 5);
        Object categoryObj = model.getValueAt(row, 6);
        try {
            String newProductname = (String) ProductnameObj;
            String newCas = (String) casObject;
            String newStructurePicPath = (String) structurePicPathObj;
            String newFormula = (String) formulaObj;
            String newRealstock = (String) realstockObj;
            double newPrice = (Double) priceObj;
            String newCategory = (String) categoryObj;
            Product Product = getProductByRow(row);
            if (Product != null) {
                Product.setProductname(newProductname);
                Product.setCas(newCas);
                Product.setStructurePicPath(newStructurePicPath);
                Product.setFormula(newFormula);
                Product.setRealstock(newRealstock);
                Product.setPrice(newPrice);
                Product.setCategory(newCategory);
                productDataAccessor.updateProduct(Product);
                JOptionPane.showMessageDialog(this, "产品信息已更新", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的信息", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Product getProductByRow(int row) {
        ArrayList<Product> Products = new ArrayList<>(productDataAccessor.getAllProducts().values());
        if (row >= 0 && row < Products.size()) {
            return Products.get(row);
        }
        return null;
    }

    private void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductManagement().setVisible(true);
            }
        });
    }
}
