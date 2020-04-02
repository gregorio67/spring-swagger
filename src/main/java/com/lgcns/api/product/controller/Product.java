/**
 * @Project : 병행검증 솔루션
 * @Class : Product.java
 * @Description :
 * @Author : kimdoy
 * @Since : Mar 26, 2020
 * @Copyright LG CNS
 *------------------------------------------------------
 *      Dodification Information
 *------------------------------------------------------
 *  Date        Modifier       Reason
 *------------------------------------------------------
 *  Mar 26, 2020     kimdoy         Initial
 *------------------------------------------------------
 */   
package com.lgcns.api.product.controller;

public class Product {
    private String code;
    private String name;
    private String description;
    private double price;
    private long stock;


    public Product(String code, String name, String description, double price, long stock) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
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


	public long getStock() {
		return stock;
	}


	public void setStock(long stock) {
		this.stock = stock;
	}
    
    
}
