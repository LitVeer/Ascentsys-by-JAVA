package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.ascent.bean.Product;
import com.ascent.service.ShoppingCartAccessorImpl;

public class ShoppingCartFrame extends JFrame {
    private JTable cartTable;
    private JButton clearCartButton;
    private JLabel totalLabel;
    private ShoppingCartAccessorImpl shoppingCart;
    private JPanel amount_control;
    private JButton button_add;
    private JButton button_del;

    public ShoppingCartFrame(ShoppingCartAccessorImpl shoppingCart) {
        this.shoppingCart = shoppingCart;
        setTitle("购物车");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeWindow();
            }
        });
        setLayout(new BorderLayout());

        initializeCartTable();
        initializeButtons();
    }

    private void initializeCartTable() {
        String[] columnNames = {"Product Name", "CAS", "Real Stock", "Price", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(model) {
            @Override  // Disable editing of cells except "Amount" column
            public boolean isCellEditable(int row, int column) {
                return column == 3;  // Only "Amount" column is editable
            }
        };
        cartTable.setFillsViewportHeight(true);

        // Add TableModelListener to detect changes in the table
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 3) {  // If "Amount" column is updated
                        updateProductAmount(row);
                    }
                }
            }
        });

        updateCartTable();
        add(new JScrollPane(cartTable), BorderLayout.CENTER);
    }

    private void initializeButtons() {
        clearCartButton = new JButton("清空购物车");
        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClearCartEvent();
            }
        });
        add(clearCartButton, BorderLayout.SOUTH);

        totalLabel = new JLabel("总价: $" + shoppingCart.getTotalPrice());
        add(totalLabel, BorderLayout.NORTH);
    }

    private void handleClearCartEvent() {
        shoppingCart.clear();
        updateCartTable();
        updateTotalLabel();
    }

    private void updateCartTable() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0);
        ArrayList<Product> products = new ArrayList<>(shoppingCart.getProducts().values());

        for (Product product : products) {
            model.addRow(new Object[] {
                product.getProductname(),
                product.getCas(),
                product.getRealstock(),
                product.getPrice(),
                product.getAmount()
            });
        }
    }

    private void updateProductAmount(int row) {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        Object amountObj = model.getValueAt(row, 3);
        try {
            int newAmount = Integer.parseInt(amountObj.toString());
            Product product = getProductByRow(row);
            if (product != null) {
                product.setAmount(newAmount);  // 更新购物车中的产品数量
                if (newAmount == 0) shoppingCart.removeProduct(product.getProductname());  // 如果数量为0，则从购物车中移除该产品
                else shoppingCart.addProduct(product);
                updateTotalLabel();  // 重新计算总价
                updateCartTable();  // 更新购物车表格
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数量", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Product getProductByRow(int row) {
        ArrayList<Product> products = new ArrayList<>(shoppingCart.getProducts().values());
        if (row >= 0 && row < products.size()) {
            return products.get(row);
        }
        return null;
    }

    private void updateTotalLabel() {
        totalLabel.setText("总价: $" + shoppingCart.getTotalPrice());
    }

    private void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ShoppingCartFrame(new ShoppingCartAccessorImpl()).setVisible(true);
            }
        });
    }
}
