package com.ascent.bean;

/**
 * 实体类Product，用来描述商品的信息类
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Product implements java.io.Serializable {

	private String productname; // 药品名称
	private String cas; // 化学文摘登记号
	private String structurePicPath; // 结构图名称
	private String formula; // 公式
	private double price; // 价格
	private String realstock; // 数量
	private String category; // 类别
	private int amount; // 购买数量 仅在购物车中使用
	
	/**
	 * 默认构造方法
	 */
	public Product() {
	}

	/**
	 * 带所有参数构造方法
	 * @param productName 药品名称
	 * @param cas 化学文摘登记号
	 * @param structurePicPath 结构图名称
	 * @param formula 公式
	 * @param price 价格
	 * @param realstock 数量
	 * @param category 类别
	 * @param amount 购买数量 仅在购物车中使用
	 */
	public Product(String productName, String cas, String structurePicPath,
			String formula, double price, String realstock, String category, int... amount) {
		this.productname = productName;
		this.cas = cas;
		this.structurePicPath = structurePicPath;
		this.formula = formula;
		this.price = price;
		this.realstock = realstock;
		this.category = category;
		this.amount = amount[0];
	}

	/**
	 * @return 药品名称
	 */
	public String getProductname() {
		return productname;
	}

	/**
	 * @param productname 设置药品名称
	 */
	public void setProductname(String productname) {
		this.productname = productname;
	}

	/**
	 * @return 化学文摘登记号
	 */
	public String getCas() {
		return cas;
	}

	/**
	 * @param cas 设置化学文摘登记号
	 */
	public void setCas(String cas) {
		this.cas = cas;
	}

	/**
	 * @return 结构图路径
	 */
	public String getStructurePicPath() {
		return structurePicPath;
	}

	/**
	 * @param structurePicPath 设置结构图路径
	 */
	public void setStructurePicPath(String structurePicPath) {
		this.structurePicPath = structurePicPath;
	}
	
	/**
	 * @return 公式
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula 设置公式
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return 价格
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price 设置价格
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return 数量
	 */
	public String getRealstock() {
		return realstock;
	}

	/**
	 * @param realstock 设置数量
	 */
	public void setRealstock(String realstock) {
		this.realstock = realstock;
	}
	
	/**
	 * @return 分类
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category 设置分类
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return 购买数量 仅在购物车中使用
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount 设置购买数量 仅在购物车中使用
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
