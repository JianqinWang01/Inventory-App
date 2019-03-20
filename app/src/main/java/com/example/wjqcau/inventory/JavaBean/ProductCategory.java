package com.example.wjqcau.inventory.JavaBean;

import java.util.ArrayList;

public class ProductCategory {
    int id;
    String title;
    ArrayList<Product>  productList=new ArrayList<>();

    public ProductCategory(){}

    public ProductCategory(int id, String title, ArrayList<Product> productList) {
        this.id = id;
        this.title = title;
        this.productList = productList;
    }

    public ProductCategory(String title, ArrayList<Product> productList) {
        this.title = title;
        this.productList = productList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return  title;
    }
}
