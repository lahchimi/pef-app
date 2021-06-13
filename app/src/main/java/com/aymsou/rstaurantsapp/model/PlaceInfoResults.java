package com.aymsou.rstaurantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlaceInfoResults {

    @SerializedName("html_attributions")
    @Expose
    private String[] html_attributions;

    @SerializedName("result")
    @Expose
    private PlaceInfo result;

    public String[] getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    public PlaceInfo getResult() {
        return result;
    }

    public void setResult(PlaceInfo result) {
        this.result = result;
    }
}
