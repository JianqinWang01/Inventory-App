package com.example.wjqcau.inventory.JavaBean;

import java.util.ArrayList;

/**
 * @author wjq
 * Mainly describe the category which including products
 */
public class ProductCategory {
    //category id
    int id;
    //declare category title
    String title;
    //products in category
    ArrayList<Product>  productList=new ArrayList<>();
   //default constructor
    public ProductCategory(){}
   //declare customered constructor
    public ProductCategory(int id, String title, ArrayList<Product> productList) {
        this.id = id;
        this.title = title;
        this.productList = productList;
    }
    //declare customered constructor
    public ProductCategory(String title, ArrayList<Product> productList) {
        this.title = title;
        this.productList = productList;
    }
  //getter and setter methods for each properties
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
  //override the tostring method
    @Override
    public String toString() {
        return  title;
    }
}
