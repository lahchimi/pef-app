package com.aymsou.rstaurantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class BookPlaceInfo {

    @SerializedName("book_email")
    @Expose
    private String email;

    @SerializedName("categories")
    @Expose
    private String[] categories;

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    @SerializedName("menu")
    @Expose
    private RestaurantMenu[] menu;

    public RestaurantMenu[] getMenu() {
        return menu;
    }

    public void setMenu(RestaurantMenu[] menu) {
        this.menu = menu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public class RestaurantMenu{

        @SerializedName("item")
        @Expose
        private String item;

        @SerializedName("price")
        @Expose
        private float price;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }

}
