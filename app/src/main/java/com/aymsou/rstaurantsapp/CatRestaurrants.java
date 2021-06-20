package com.aymsou.rstaurantsapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.aymsou.rstaurantsapp.adapters.RestaurantsAdapter;
import com.aymsou.rstaurantsapp.model.Restaurant;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatRestaurrants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_restaurrants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Fresco.initialize(this);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("catName"));
        String CatId = intent.getStringExtra("catId");
        final RecyclerView restaurantsRecyclerView = (RecyclerView)findViewById(R.id.restaurantsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        restaurantsRecyclerView.setLayoutManager(mLayoutManager);
        restaurantsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
        final ProgressBar placeInfoProgress = (ProgressBar)findViewById(R.id.placeInfoProgress);

        Call<List<Restaurant>> call = api.getCategoryRestaurants(CatId);
        Log.d("xxx CatRestaurrants xxx", call.toString() );
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()){
                    placeInfoProgress.setVisibility(View.GONE);
                    List<Restaurant> restaurants = response.body();
                    Log.d("LengthRests", restaurants.size()+"");
                    RestaurantsAdapter rAdapter = new RestaurantsAdapter(restaurants, CatRestaurrants.this);
                    restaurantsRecyclerView.setAdapter(rAdapter);
                }

                Log.d("LengthRests xxx", response.toString() );
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.d("LengthRests", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
