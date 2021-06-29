package com.aymsou.rstaurantsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

import com.aymsou.rstaurantsapp.adapters.RestaurantsAdapter;
import com.aymsou.rstaurantsapp.model.Restaurant;
import com.aymsou.rstaurantsapp.utils.DatabaseHelper;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class Bookmarks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Fresco.initialize(this);

        DatabaseHelper db = new DatabaseHelper(this);

        final RecyclerView restaurantsRecyclerView = (RecyclerView)findViewById(R.id.restaurantsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        restaurantsRecyclerView.setLayoutManager(mLayoutManager);
        restaurantsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Restaurant> restaurants = db.getAllRestaurants();
        RestaurantsAdapter rAdapter = new RestaurantsAdapter(restaurants, Bookmarks.this);
        restaurantsRecyclerView.setAdapter(rAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
