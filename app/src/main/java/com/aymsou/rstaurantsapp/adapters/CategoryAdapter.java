package com.aymsou.rstaurantsapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aymsou.rstaurantsapp.CatRestaurrants;
import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.model.Category;

import java.util.ArrayList;
import java.util.List;



public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<Category> categories = new ArrayList<>();
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView categoryName;
        public RelativeLayout categoryLayout;

        public ViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryLayout = (RelativeLayout) view.findViewById(R.id.categoryLayout);
        }

    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        final Category cat = categories.get(position);
        holder.categoryName.setText(cat.getName());
        holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick Cat", "onClick: xxx");
                Intent intent = new Intent(ctx, CatRestaurrants.class);
                intent.putExtra("catName", cat.getName());
                intent.putExtra("catId", cat.getId());
                ctx.startActivity(intent);
            }
        });
    }

    public CategoryAdapter(List<Category> categories, Context ctx) {
        this.categories = categories;
        this.ctx = ctx;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
