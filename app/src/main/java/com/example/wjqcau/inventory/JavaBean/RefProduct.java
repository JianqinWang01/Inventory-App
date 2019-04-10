package com.example.wjqcau.inventory.JavaBean;

/**
 * @author wjq
 * Mainly hold the information which retrieved from Walmart API
 * This class used in PriceReferFragment
 */

public class RefProduct {
    //declare product name
    private String name;
    //declare the product price
    private String salePrice;
    //declare the variable to hold image URL
    private String imageUrl;

    private String category;
    //Constructor
    public RefProduct(String name, String salePrice, String imageUrl, String category) {
        this.name = name;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.category = category;
    }
   //Declare getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Declare getter and setter methods
    public String getImageUrl() {
        return imageUrl;
    }
    //Declare getter and setter methods
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSalePrice() {
        return salePrice;
    }
    //Declare getter and setter methods
    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }
    //Declare getter and setter methods
    public String getCategory() {
        return category;
    }

    public void setCategoryD(String category) {
        this.category = category;
    }
}
