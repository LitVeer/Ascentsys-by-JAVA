package com.ascent.ui;

import javax.swing.*;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ascent.bean.Product;
import com.ascent.service.ChatServer;
import com.ascent.service.ProductDataAccessorImpl;
import com.ascent.service.ShoppingCartAccessorImpl;

public class MainFrame extends JFrame {
    private HashMap<String, Product> products;
    private JList<String> productList;
    private DefaultListModel<String> listModel;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private ShoppingCartAccessorImpl shoppingCart;
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenu menu_help;
    private JMenuItem menuItem_export;
    private JMenuItem menuItem_about;
    private JMenuItem menuItem_customerService;
    private JMenuItem menuItem_backStage;

    public MainFrame() {
        setTitle("Welcome to AscentSys! ");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        menuBar = new JMenuBar();
        menu_file = new JMenu("文件");
        menuItem_export = new JMenuItem("导出购物车");
        // menuItem_export.addActionListener(e -> handleExportEvent());
        menu_file.add(menuItem_export);
        menuBar.add(menu_file);

        menu_help = new JMenu("帮助");
        menuItem_about = new JMenuItem("关于");
        menuItem_about.addActionListener(e -> handleAboutEvent());
        menu_help.add(menuItem_about);
        menuItem_customerService = new JMenuItem("客服");
        menuItem_customerService.addActionListener(e -> handleCustomerServiceEvent());
        menu_help.add(menuItem_customerService);
        menuItem_backStage = new JMenuItem("后台管理");
        menuItem_backStage.addActionListener(e -> handleBackStageEvent());
        menu_help.add(menuItem_backStage);
        menuBar.add(menu_help);
        add(menuBar, BorderLayout.NORTH);


        shoppingCart = new ShoppingCartAccessorImpl();
        initializeProductList();

        addToCartButton = new JButton("添加到购物车");
        addToCartButton.addActionListener(e -> handleAddToCartEvent());
        add(addToCartButton, BorderLayout.SOUTH);

        viewCartButton = new JButton("查看购物车");
        viewCartButton.addActionListener(e -> handleViewCartEvent());
        add(viewCartButton, BorderLayout.EAST);
    }

    private void initializeProductList() {
        ProductDataAccessorImpl productDataAccessor = new ProductDataAccessorImpl();
        products = productDataAccessor.getAllProducts();

        listModel = new DefaultListModel<>();
        for (Product product : products.values()) {
            listModel.addElement(product.getProductname());
        }

        productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // 允许单选
        productList.setVisibleRowCount(10);

        productList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    String productName = (String) value;
                    Product product = products.get(productName);
                    if (product != null) {
                        // 使用 HTML 和 CSS 来控制格式
                        String text = "<html>"
                                + "<div style='display: flex; justify-content: space-between;'>"
                                + "<span>" + product.getProductname() + "</span> | "
                                + "<span>" + product.getCas() + "</span> | "
                                + "<span>" + product.getFormula() + "</span> | "
                                + "<span>" + product.getStructurePicPath() + "</span> | "
                                + "<span>" + product.getRealstock() + "</span> | "
                                + "<span>" + product.getCategory() + "</span> | "
                                + "<span>" + String.format("%.2f", product.getPrice()) + "</span>"
                                + "</div>"
                                + "</html>";
                        setText(text);
                        setOpaque(true);
                        setBackground(isSelected ? Color.blue : Color.white);
                        setForeground(isSelected ? Color.white : Color.black);
                    }
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(productList);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void handleAddToCartEvent() {
        // 获取选中的产品名称
        String selectedProductName = productList.getSelectedValue();
        if (selectedProductName != null) {
            // 从HashMap中找到对应的产品对象
            Product selectedProduct = products.get(selectedProductName);
            // 更新数量 + 1
            selectedProduct.setAmount(selectedProduct.getAmount() + 1);
            // 添加到购物车
            shoppingCart.addProduct(selectedProduct);
            // 更新查看购物车按钮的文本
            viewCartButton.setText("查看购物车 (" + shoppingCart.getProducts().size() + ")");
        }
    }

    private void handleViewCartEvent() {
        new Thread(() -> new ShoppingCartFrame(shoppingCart).setVisible(true)).start();
    }

    //private void handleExportEvent() {}

    private void handleAboutEvent() {
        JOptionPane.showMessageDialog(this, "关于", "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleCustomerServiceEvent() {
        SwingUtilities.invokeLater(() -> new Thread(() -> new ChatClient()).start());
        SwingUtilities.invokeLater(() -> new Thread(() -> new ChatServer()).start());
    }

    private void handleBackStageEvent() {
        new Thread(() -> new AdminAuthenrity().setVisible(true)).start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}