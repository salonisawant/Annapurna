package com.annapurna.model;

public class Product {
	private int productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public Product(int productId, String name, String description, double price, String category, boolean available) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.available = available;
	}
	public Product() {
		// TODO Auto-generated constructor stub
	}
    
    
}
