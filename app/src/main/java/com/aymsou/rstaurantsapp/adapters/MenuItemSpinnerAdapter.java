package com.aymsou.rstaurantsapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aymsou.rstaurantsapp.model.BookMenuItem;

import java.util.ArrayList;
import java.util.List;


public class MenuItemSpinnerAdapter extends ArrayAdapter<BookMenuItem> {

    private Context context;
    private List<BookMenuItem> myObjs = new ArrayList();

    public MenuItemSpinnerAdapter(Context context, int textViewResourceId, List<BookMenuItem> myObjs) {
        super(context, textViewResourceId, myObjs);
        this.context = context;
        this.myObjs = myObjs;
    }

    public int getCount(){
        return myObjs.size();
    }

    public BookMenuItem getItem(int position){
        return myObjs.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setPadding(40,40,40,40);
        label.setTextColor(context.getResources().getColor(android.R.color.black));
        label.setText(myObjs.get(position).getItemName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setPadding(40,40,40,40);
        label.setTextColor(context.getResources().getColor(android.R.color.black));
        label.setText(myObjs.get(position).getItemName());
        return label;
    }

}
