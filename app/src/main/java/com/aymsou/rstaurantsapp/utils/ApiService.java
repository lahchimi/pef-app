package com.aymsou.rstaurantsapp.utils;

import com.aymsou.rstaurantsapp.model.BookPlaceInfo;
import com.aymsou.rstaurantsapp.model.Category;
import com.aymsou.rstaurantsapp.model.MapResults;
import com.aymsou.rstaurantsapp.model.PlaceInfo;
import com.aymsou.rstaurantsapp.model.PlaceInfoResults;
import com.aymsou.rstaurantsapp.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {

    @GET("nearbysearch/json?radius=936000&type=restaurant&key="+Const.API_KEY)
    Call<MapResults> getNearbyRestaurants(@Query("location") String location);

    @FormUrlEncoded
    @POST("restaurant")
    Call<PlaceInfo> getRestaurantDetails(@Field("rest_id") String rest_id);

    @FormUrlEncoded
    @POST("restaurant_menu")
    Call<BookPlaceInfo> getBookPlaceInfo(@Field("gmap_place_id") String gmap_place_id);

    @FormUrlEncoded
    @POST("send_book_request")
    Call<String> sendBookRequest(@Field("email") String email, @Field("fullname") String fullname, @Field("f_body") String fbody, @Field("total") Float total );

    @POST("categories")
    Call<List<Category>> getCategories();

    @FormUrlEncoded
    @POST("category")
    Call<List<Restaurant>> getCategoryRestaurants(@Field("cat_id") String cat_id);

    @FormUrlEncoded
    @POST("send_restaurant")
    Call<String> sendRestaurantRequest(@Field("lon") String lon, @Field("lat") String lat, @Field("email") String email, @Field("name") String name, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("send_message")
    Call<String> sendMessage(@Field("email") String email, @Field("fname") String fname, @Field("msg") String msg);

}
