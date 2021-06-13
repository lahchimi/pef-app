package com.aymsou.rstaurantsapp.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.model.BookPlaceInfo;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    private BookPlaceInfo.RestaurantMenu[] menuList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item, price;

        public ViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(R.id.item);
            price = (TextView) view.findViewById(R.id.price);
        }
    }

    public MenuAdapter(BookPlaceInfo.RestaurantMenu[] menuList){
        this.menuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookPlaceInfo.RestaurantMenu menu = menuList[position];
        holder.item.setText(menu.getItem());
        holder.price.setText(String.valueOf(menu.getPrice())+" MAD");
    }

    @Override
    public int getItemCount() {
        return menuList == null ? 0 : menuList.length;
    }

}
