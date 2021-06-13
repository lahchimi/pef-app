package com.aymsou.rstaurantsapp.model;



public class FactureItem{
    public int quantity;
    public float price;
    public String itemName;

    public FactureItem(int quantity, float price, String itemName) {
        this.quantity = quantity;
        this.price = price;
        this.itemName = itemName;
    }
}