package com.example.wjqcau.inventory.JavaBean;

public class RefProduct {
    private String name;
    private String salePrice;
    private String imageUrl;

    private String category;

    public RefProduct(String name, String salePrice, String imageUrl, String category) {
        this.name = name;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategoryD(String category) {
        this.category = category;
    }
}
