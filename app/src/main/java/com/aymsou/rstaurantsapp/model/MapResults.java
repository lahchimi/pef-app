package com.aymsou.rstaurantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class MapResults {

	@SerializedName("html_attributions")
    @Expose
    private String[] html_attributions;

	@SerializedName("next_page_token")
    @Expose
    private String next_page_token;

    @SerializedName("results")
    @Expose
    private ArrayList<MapPlace> results = new ArrayList<>();

    public String[] getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public ArrayList<MapPlace> getResults() {
        return results;
    }

    public void setResults(ArrayList<MapPlace> results) {
        this.results = results;
    }
}