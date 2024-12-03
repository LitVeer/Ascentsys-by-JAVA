package com.ascent;

public interface config {
    
    //基本配置

    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String DB_URL = "jdbc:mysql://localhost:3306/the_datatable_name?serverTimezone=UTC";

    public static final String DB_USER = "root";

    public static final String DB_PASSWORD = "your_password";

    //服务器配置

    public static final int DEFAULT_PORT = 5150;

    public static final String DEFAULT_HOST = "localhost";

    //协议配置

    public static final int OP_GET_PRODUCT_CATEGORIES = 100;

    public static final int OP_GET_PRODUCTS = 101;

    public static final int OP_GET_USERS = 102;

    public static final int OP_ADD_USERS = 103;

    public static final int OP_DEL_USERS = 104;

    public static final int OP_UPDATE_USER_AUTHORIY = 105;


    public static final int OP_ADD_PRODUCT = 106;

    public static final int OP_DEL_PRODUCT = 107;

    public static final int OP_UPDATE_PRODUCT = 108;
    
}
