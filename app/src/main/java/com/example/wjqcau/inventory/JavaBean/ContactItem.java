package com.example.wjqcau.inventory.JavaBean;

public class ContactItem {
    private int itemTitle;
    private int imageID;


    public ContactItem(int itemTitle, int imageID) {
        this.itemTitle = itemTitle;
        this.imageID = imageID;
    }

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
