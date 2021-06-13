package com.aymsou.rstaurantsapp.model;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class MapPlace {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("place_id")
    @Expose
    private String place_id;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("geometry")
    @Expose
    private GeoMetry geometry;

    public GeoMetry getGeometry() {
        return geometry;
    }

    public void setGeometry(GeoMetry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public class GeoMetry{

        @SerializedName("location")
        @Expose
        private LocationData location;

        public LocationData getLocation() {
            return location;
        }

        public void setLocation(LocationData location) {
            this.location = location;
        }

        public class LocationData{

            @SerializedName("lat")
            @Expose
            private double lat;

            @SerializedName("lng")
            @Expose
            private double lng;

            @Override
            public String toString() {
                return "LocationData{" +
                        "lat=" + lat +
                        ", lng=" + lng +
                        '}';
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

        }

    }

    public static String getDistance(Location loc1, Location loc2){
        int distance_is = (int)Math.floor(loc1.distanceTo(loc2));
        return distance_is < 1000 ? String.valueOf(distance_is)+" m" : String.valueOf(distance_is/1000)+" Km";
    }

}
