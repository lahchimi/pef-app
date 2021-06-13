package com.aymsou.rstaurantsapp.model;



public class BookMenuItem {
    public String itemName;
    public float itemPrice;

    public BookMenuItem(String itemName, float itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }
}
