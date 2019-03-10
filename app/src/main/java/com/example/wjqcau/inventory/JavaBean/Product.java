package com.example.wjqcau.inventory.JavaBean;

public class Product {
    int id;
    int categoryID;
    String name;
    String price;
    //int prodImage;
    String amount;
    String imageUrl;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Product(){}
    public Product(String name,String price,String amount,String imageUrl,int cateoryId){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amount=amount;
        this.categoryID=cateoryId;
    }

    public Product(String name, String price, String amount,String imageUrl) {

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amount=amount;
    }

    public Product(int id,String name, String price,String amount, String imageUrl,int categoryID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryID=categoryID;
        this.amount=amount;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
