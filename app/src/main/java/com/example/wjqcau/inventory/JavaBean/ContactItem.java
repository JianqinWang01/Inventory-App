package com.example.wjqcau.inventory.JavaBean;

/**
 * @author wjq
 * Date created: 2019-03-03
 * @description Used in contact fragment, for each contact including title and icon
 */
public class ContactItem {
    //properties : item tile and its icon
    private int itemTitle;
    private int imageID;

  //Constructor
    public ContactItem(int itemTitle, int imageID) {
        this.itemTitle = itemTitle;
        this.imageID = imageID;
    }
// getter and setter method
    public int getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(int itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
