package com.example.wjqcau.inventory.JavaBean;

/**
 * @author wjq
 * This Mainly hold the information for searched result
 * This is simplified product class
 * This class used in search fragment
 */
public class SearchResult {
 //declare properties for the searched result
    private int prodId;
    private String prodPrice;
    private String prodName;
    private String cateName;
 //declare constructor
    public SearchResult(String prodPrice, String prodName, String cateName) {
        this.prodPrice = prodPrice;
        this.prodName = prodName;
        this.cateName = cateName;
    }
    //declare constructor

    public SearchResult(int prodId,String prodName, String prodPrice,  String cateName) {

        this.prodId = prodId;
        this.prodPrice = prodPrice;
        this.prodName = prodName;
        this.cateName = cateName;
    }
 //declare getter and setter method
    public int getProdId() {

        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }
    //declare getter and setter method

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }
    //declare getter and setter method

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }



}
