package com.example.wjqcau.inventory.JavaBean;

public class Product {
    int id;
    int categoryID;
    String name;
    String price;
    int prodImage;
    String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Product(){}

    public Product(String name, String price, String amount,int prodImage) {

        this.name = name;
        this.price = price;
        this.prodImage = prodImage;
        this.amount=amount;
    }

    public Product(int id,String name, String price,String amount, int prodImage,int categoryID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.prodImage = prodImage;
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

    public int getProdImage() {
        return prodImage;
    }

    public void setProdImage(int prodImage) {
        this.prodImage = prodImage;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
