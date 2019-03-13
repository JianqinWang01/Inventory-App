package com.example.wjqcau.inventory.JavaBean;

public class SearchResult {
    private int prodId;

    public SearchResult(String prodPrice, String prodName, String cateName) {
        this.prodPrice = prodPrice;
        this.prodName = prodName;
        this.cateName = cateName;
    }

    public SearchResult(int prodId,String prodName, String prodPrice,  String cateName) {

        this.prodId = prodId;
        this.prodPrice = prodPrice;
        this.prodName = prodName;
        this.cateName = cateName;
    }

    public int getProdId() {

        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

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

    private String prodPrice;
    private String prodName;
    private String cateName;

}
