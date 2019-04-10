package com.example.wjqcau.inventory.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wjq
 * Mainly descript the products under a certain category
 */
public class Product implements Parcelable {
   //product id
    int id;
    //category id this product belongs to
    int categoryID;
    //product name ,price,amount
    String name;
    String price;
    //int prodImage;
    String amount;
    //Image URL in stored
    String imageUrl;
    //product unit
    String unit;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
   //default constructor
    public Product(){}
    //customered constructor
    public Product(String name,String price,String amount,String imageUrl,String unit,int cateoryId){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amount=amount;
        this.categoryID=cateoryId;
        this.unit=unit;
    }
   //custormered constructor
    public Product(String name, String price, String amount,String imageUrl,String unit) {

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amount=amount;
        this.unit=unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    //custormered constructor
    public Product(int id, String name, String price, String amount, String imageUrl, String unit,int categoryID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryID=categoryID;
        this.amount=amount;
        this.unit=unit;

    }


 //getter and setter method

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

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest
     * @param flags
     * implement the parcel interface
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.categoryID);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.amount);
        dest.writeString(this.imageUrl);
        dest.writeString(this.unit);
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.categoryID = in.readInt();
        this.name = in.readString();
        this.price = in.readString();
        this.amount = in.readString();
        this.imageUrl = in.readString();
        this.unit = in.readString();
    }
  //Creator in object
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
