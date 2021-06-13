package com.aymsou.rstaurantsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aymsou.rstaurantsapp.PlaceDetails;
import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.model.Restaurant;
import com.aymsou.rstaurantsapp.utils.LastLocationM;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>{


    private List<Restaurant> restaurants = new ArrayList<>();
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView placeName;
        public Button details;
        public SimpleDraweeView placeImg;
        public RatingBar ratingBar;
        public RelativeLayout placeInfoProgress;

        public ViewHolder(View view) {
            super(view);
            placeName = (TextView) view.findViewById(R.id.placeName);
            details = (Button) view.findViewById(R.id.details);
            placeImg = (SimpleDraweeView) view.findViewById(R.id.placeImg);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            placeInfoProgress = (RelativeLayout) view.findViewById(R.id.placeInfoProgress);
        }

    }

    @Override
    public RestaurantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_restaurant, parent, false);
        return new RestaurantsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RestaurantsAdapter.ViewHolder holder, int position) {
        final Restaurant rest = restaurants.get(position);

        holder.placeInfoProgress.setVisibility(View.GONE);
        holder.placeName.setVisibility(View.VISIBLE);
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.placeImg.setVisibility(View.GONE);
        holder.details.setVisibility(View.VISIBLE);

        holder.placeName.setText(rest.getName());
        holder.ratingBar.setRating(5); //details.getRating()
        //holder.placeImg.setImageURI(Uri.parse(String.format(Const.PLACE_PHOTO_URI, String.valueOf(image.getWidth()), image.getPhotoReference())));
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, PlaceDetails.class);
                intent.putExtra("place_id", rest.getId());
                intent.putExtra("restaurant_id", rest.getId());
                intent.putExtra("current_location", LastLocationM.lastLocation);
                ctx.startActivity(intent);
            }
        });
    }

    public RestaurantsAdapter(List<Restaurant> restaurants, Context ctx) {
        this.restaurants = restaurants;
        this.ctx = ctx;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
