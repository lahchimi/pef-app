package com.aymsou.rstaurantsapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aymsou.rstaurantsapp.model.Restaurant;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "appManagerData";
    private static final String TABLE_RESTAURANTS = "restaurants";

    private static final String KEY_ID = "id";
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_RESTO_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACE_ID + " TEXT," +  KEY_RESTO_NAME + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }

    public boolean isInFavorites(String placeid) {
        String countQuery = "SELECT  * FROM " + TABLE_RESTAURANTS+" WHERE "+KEY_PLACE_ID+" ='"+placeid+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt > 0;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restList = new ArrayList<Restaurant>();
        String selectQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant rest = new Restaurant();
                rest.setId( Integer.parseInt(cursor.getString(1)) );
                rest.setName(cursor.getString(2));
                restList.add(rest);
            } while (cursor.moveToNext());
        }
        return restList;
    }

    public void addPlace(String placeid, String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        // onUpgrade(db, 1,2);
        // onCreate(db);

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, placeid);
        values.put(KEY_RESTO_NAME, name);
        db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
    }

    public void deleteRestaurant(String placeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_PLACE_ID + " = ?", new String[] { String.valueOf(placeid) });
        db.close();
    }

}
