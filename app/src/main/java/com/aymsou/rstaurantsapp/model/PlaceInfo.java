package com.aymsou.rstaurantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlaceInfo{

    @SerializedName("periods")
    @Expose
    private String[][] periods;

    public String getPeriodText(){
        String txt = "";
        txt += "Lundi: "+this.periods[0][0]+" - "+this.periods[0][1];
        txt += "\nMardi: "+this.periods[1][0]+" - "+this.periods[1][1];
        txt += "\nMercredi: "+this.periods[2][0]+" - "+this.periods[2][1];
        txt += "\nJeudi: "+this.periods[3][0]+" - "+this.periods[3][1];
        txt += "\nVendredi: "+this.periods[4][0]+" - "+this.periods[4][1];
        txt += "\nSamedi: "+this.periods[5][0]+" - "+this.periods[5][1];
        txt += "\nDimanche: "+this.periods[6][0]+" - "+this.periods[6][1];
        return txt;
    }

    public String[][] getPeriods() {
        return periods;
    }

    public void setPeriods(String[][] periods) {
        this.periods = periods;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating(){
        double randomDouble = Math.random();
        randomDouble = randomDouble * 5 + 1;
        int randomInt = (int) randomDouble;
        return randomInt;
    }

    @SerializedName("bookingEmail")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
