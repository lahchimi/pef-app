package com.aymsou.rstaurantsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aymsou.rstaurantsapp.adapters.MenuAdapter;
import com.aymsou.rstaurantsapp.adapters.MenuItemSpinnerAdapter;
import com.aymsou.rstaurantsapp.adapters.PlacePhotosAdapter;
import com.aymsou.rstaurantsapp.model.BookMenuItem;
import com.aymsou.rstaurantsapp.model.BookPlaceInfo;
import com.aymsou.rstaurantsapp.model.Facture;
import com.aymsou.rstaurantsapp.model.FactureItem;
import com.aymsou.rstaurantsapp.model.PlaceInfo;
import com.aymsou.rstaurantsapp.model.PlaceInfoResults;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.DatabaseHelper;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetails extends AppCompatActivity {

    TextView placeName, placeAddress, placeWebData, placePhoneData, placeIsOpenNow, weekText, menuUnav;
    RatingBar ratingBar;
    ProgressBar placeInfoProgress, placeMapProgress, placePhotosProgress, placeWebProgress, placeOtherProgress, placeMenuProgress;
    //MapView placeInfoMapView;
    Location mLastLocation;
    ViewPager viewpager;
    CircleIndicator indicator;
    CardView photosCard, cardOther, CardInfoContact;
    LinearLayout placePhone, placeWebsite, placeOther;
    BookPlaceInfo book_place_info;
    Button bookBtn;
    RecyclerView menuRecyclerView;
    String place_id;
    String place_name;
    EditText inputFullname, inputPhone;
    TextInputLayout layoutFullname, layoutPhone;
    Facture facture;
    FloatingActionButton favfab;
    DatabaseHelper db;
    boolean isfavorited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_place_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //placeInfoMapView = (MapView) findViewById(R.id.placeInfoMapView);
        placeName = (TextView) findViewById(R.id.placeName);
        placeAddress = (TextView) findViewById(R.id.placeAddress);
        placeWebData = (TextView) findViewById(R.id.placeWebData);
        placePhoneData = (TextView) findViewById(R.id.placePhoneData);
        placeIsOpenNow = (TextView) findViewById(R.id.placeIsOpenNow);
        weekText = (TextView) findViewById(R.id.weekText);
        menuUnav = (TextView) findViewById(R.id.menuUnav);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        placeInfoProgress = (ProgressBar) findViewById(R.id.placeInfoProgress);
        placeMapProgress = (ProgressBar) findViewById(R.id.placeMapProgress);
        placePhotosProgress = (ProgressBar) findViewById(R.id.placePhotosProgress);
        placeWebProgress = (ProgressBar) findViewById(R.id.placeWebProgress);
        placeOtherProgress = (ProgressBar) findViewById(R.id.placeOtherProgress);
        placeMenuProgress = (ProgressBar) findViewById(R.id.placeMenuProgress);
        viewpager = (ViewPager) findViewById(R.id.vpPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        photosCard = (CardView) findViewById(R.id.photosCard);
        cardOther = (CardView) findViewById(R.id.cardOther);
        CardInfoContact = (CardView) findViewById(R.id.CardInfoContact);
        placePhone = (LinearLayout) findViewById(R.id.placePhone);
        placeWebsite = (LinearLayout) findViewById(R.id.placeWebsite);
        placeOther = (LinearLayout) findViewById(R.id.placeOther);
        bookBtn = (Button) findViewById(R.id.BookBtn);
        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerView);
        favfab = (FloatingActionButton) findViewById(R.id.favfab);

        db = new DatabaseHelper(this);

        facture = new Facture();

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_place_info == null){
                    AlertDialog alertDialog = new AlertDialog.Builder(PlaceDetails.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.book));
                    alertDialog.setMessage(getResources().getString(R.string.book_unavailiable));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PlaceDetails.this);
                    LayoutInflater inflater = PlaceDetails.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.book_data, null);
                    inputFullname = (EditText)dialogView.findViewById(R.id.inputFullname);
                    inputPhone = (EditText)dialogView.findViewById(R.id.inputPhone);
                    layoutFullname = (TextInputLayout)dialogView.findViewById(R.id.input_layout_name);
                    layoutPhone = (TextInputLayout)dialogView.findViewById(R.id.input_layout_phone);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setTitle(getResources().getString(R.string.book_now));
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilder.setPositiveButton(getResources().getString(R.string.next), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(inputFullname.getText().toString().trim().isEmpty() ||inputPhone.getText().toString().trim().isEmpty())
                                return;
                            facture.Fullname = inputFullname.getText().toString();
                            facture.PhoneNumber = inputPhone.getText().toString();
                            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(PlaceDetails.this);
                            LayoutInflater inflater = PlaceDetails.this.getLayoutInflater();
                            final View dialogView2 = inflater.inflate(R.layout.book_item_layout, null);
                            dialogBuilder2.setView(dialogView2);

                            final LinearLayout table = (LinearLayout)dialogView2.findViewById(R.id.tableLinear);
                            Button add_new_item = (Button) dialogView2.findViewById(R.id.add_new_item);
                            add_new_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LayoutInflater inflater = (LayoutInflater) PlaceDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View vinflate = inflater.inflate(R.layout.book_item_row, null);
                                    Spinner spnrdata = (Spinner)vinflate.findViewById(R.id.spinner);
                                    Button del_btn = (Button)vinflate.findViewById(R.id.del_btn);
                                    del_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            LinearLayout linearParent = (LinearLayout) v.getParent().getParent();
                                            table.removeView(linearParent);
                                        }
                                    });
                                    Spinner qtyspnr = (Spinner)vinflate.findViewById(R.id.qty);
                                    List<String> spinnerArray =  new ArrayList<String>();
                                    for(int i=1;i<=20;i++)
                                        spinnerArray.add(String.valueOf(i));
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlaceDetails.this, android.R.layout.simple_spinner_item, spinnerArray);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    qtyspnr.setAdapter(adapter);
                                    qtyspnr.setSelection(0);

                                    ArrayList<BookMenuItem> menuItems = new ArrayList<>();
                                    for (BookPlaceInfo.RestaurantMenu book_r_menu : book_place_info.getMenu()) {
                                        menuItems.add(new BookMenuItem(book_r_menu.getItem(), book_r_menu.getPrice()));
                                    }
                                    MenuItemSpinnerAdapter spnradapter = new MenuItemSpinnerAdapter(PlaceDetails.this, android.R.layout.simple_spinner_item, menuItems);
                                    spnrdata.setAdapter(spnradapter);
                                    spnrdata.setSelection(0);

                                    LinearLayout rowView = (LinearLayout)vinflate;
                                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.toggleSoftInputFromWindow(rowView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                    table.addView(rowView);
                                }
                            });

                            dialogBuilder2.setPositiveButton(getResources().getString(R.string.book_item), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    facture.items.clear();
                                    int j= 0;
                                    for (int i = 1; i < table.getChildCount(); i++) {
                                        View v = table.getChildAt(i);
                                        if (v instanceof LinearLayout) {
                                            j++;
                                            Spinner itemData = (Spinner)v.findViewById(R.id.spinner);
                                            Spinner itemQty = (Spinner)v.findViewById(R.id.qty);

                                            int itemQuantity = Integer.valueOf(itemQty.getSelectedItem().toString());
                                            BookMenuItem itemMenuBook = (BookMenuItem)itemData.getSelectedItem();
                                            facture.items.add(new FactureItem(itemQuantity, itemMenuBook.itemPrice, itemMenuBook.itemName));
                                        }
                                    }
                                    if(j==0)
                                        return;
                                    dialog.dismiss();
                                    //Show alertdialog 3
                                    AlertDialog alertDialog3 = new AlertDialog.Builder(PlaceDetails.this).create();
                                    alertDialog3.setTitle(getResources().getString(R.string.book_now));
                                    alertDialog3.setMessage("Totale et : "+String.valueOf(facture.calculateTotal())+" MAD");
                                    alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.next),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final ProgressDialog progress = new ProgressDialog(PlaceDetails.this);
                                                    progress.setMessage(getResources().getString(R.string.please_wait));
                                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progress.setIndeterminate(true);
                                                    progress.show();

                                                    ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                                                    Call<String> call = api.sendBookRequest(book_place_info.getEmail(), facture.Fullname, facture.toString(), facture.calculateTotal());
                                                    call.enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            if(response.isSuccessful()){
                                                                progress.dismiss();
                                                                Toast.makeText(PlaceDetails.this, getResources().getString(R.string.book_sent_success), Toast.LENGTH_LONG).show();
                                                            }
                                                            Log.d("TAG", "onResponse:sendBookRequest "+ response.toString());
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {

                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog3.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog3.show();
                                }
                            });

                            dialogBuilder2.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.dismiss();
                            AlertDialog alertDialog2 = dialogBuilder2.create();
                            alertDialog2.show();
                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });


        /*placeInfoMapView.onCreate(savedInstanceState);
        placeInfoMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        mLastLocation = getIntent().getParcelableExtra("current_location");

        place_id = getIntent().getStringExtra("place_id");
        place_name = getIntent().getStringExtra("place_name");

        Log.d("TAG", "onClick: RestaurantsAdapter => place_id = "+ place_id );
        Log.d("TAG", "onClick: RestaurantsAdapter => mLastLocation = "+ mLastLocation );
        isfavorited = db.isInFavorites(place_id);
        if(isfavorited)
            favfab.setImageDrawable(getResources().getDrawable(R.drawable.heart));
        else
            favfab.setImageDrawable(getResources().getDrawable(R.drawable.heart_outline));
        favfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfavorited) {
                    db.deleteRestaurant(place_id);
                    isfavorited = false;
                    favfab.setImageDrawable(getResources().getDrawable(R.drawable.heart_outline));
                }else{
                    db.addPlace(place_id,place_name);
                    isfavorited = true;
                    favfab.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                }
                Log.d("TAG favfab", "onClick: RestaurantsAdapter => place_id = "+ place_id );
                Log.d("TAG favfab", "onClick: RestaurantsAdapter => mLastLocation = "+ mLastLocation );
            }
        });

        ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);

        int restaurant_id = getIntent().getIntExtra("restaurant_id", 1);

        Log.d("TAG", "onCreate:restaurant_id = "+restaurant_id);
        Call<PlaceInfo> call = api.getRestaurantDetails(restaurant_id+"");

        call.enqueue(new Callback<PlaceInfo>() {
            @Override
            public void onResponse(Call<PlaceInfo> call, Response<PlaceInfo> response) {
                if (response.isSuccessful()) {
                    placeInfoProgress.setVisibility(View.GONE);
                    placePhotosProgress.setVisibility(View.GONE);
                    placeWebProgress.setVisibility(View.GONE);
                    placeOtherProgress.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.VISIBLE);
                    bookBtn.setVisibility(View.VISIBLE);

                    final PlaceInfo details = response.body();
                    Log.d("TAG_PlaceInfo", "onResponse: "+response.body());
                    placeName.setText(details.getName());
                    placeAddress.setText(details.getEmail());
                    ratingBar.setRating(details.getRating());

                    placePhone.setVisibility(View.VISIBLE);
                    placePhoneData.setText(details.getPhone());

                    placeOther.setVisibility(View.VISIBLE);
                    weekText.setText(details.getPeriodText());

                    /*placeInfoMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(details.getLat(), details.getLng());
                            Location marker_location = new Location("locA");
                            marker_location.setLatitude(latLng.latitude);
                            marker_location.setLongitude(latLng.longitude);
                            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            Marker marker = googleMap.addMarker(new MarkerOptions().title(details.getName()).position(latLng));
                            marker.setSnippet(String.format(getResources().getString(R.string.distance_from_you), MapPlace.getDistance(marker_location, mLastLocation)));
                            marker.showInfoWindow();
                            placeInfoMapView.setVisibility(View.VISIBLE);
                            placeMapProgress.setVisibility(View.GONE);
                        }
                    });*/

                    /*if (details.getPhotos() != null) {
                        viewpager.setAdapter(new PlacePhotosAdapter(PlaceDetails.this, details.getPhotos()));
                        indicator.setViewPager(viewpager);
                    } else {
                        photosCard.setVisibility(View.GONE);
                    }

                    if(details.getFormattedPhoneNumber() != null || details.getWebsite()!=null) {
                        if(details.getFormattedPhoneNumber() != null){
                            if(details.getFormattedPhoneNumber().trim().equals(""))
                                placePhone.setVisibility(View.GONE);
                            else{
                                placePhone.setVisibility(View.VISIBLE);
                                placePhoneData.setText(details.getFormattedPhoneNumber());
                            }
                        }
                        if(details.getWebsite() != null){
                            if(details.getWebsite().trim().equals(""))
                                placeWebsite.setVisibility(View.GONE);
                            else{
                                placeWebsite.setVisibility(View.VISIBLE);
                                placeWebData.setText(details.getWebsite());
                            }
                        }
                        if (details.getFormattedPhoneNumber().trim().equals("") && details.getWebsite().trim().equals("")) {
                            CardInfoContact.setVisibility(View.GONE);
                        }
                    }else{
                        CardInfoContact.setVisibility(View.GONE);
                    }

                    if(details.getOpeningHours() == null)
                        cardOther.setVisibility(View.GONE);
                    else {
                        placeOther.setVisibility(View.VISIBLE);
                        placeIsOpenNow.setText(details.getOpeningHours().isOpenNow() ? getResources().getString(R.string.yes) : getResources().getString(R.string.nop));
                        if (details.getOpeningHours().getWeekdayText() != null)
                            weekText.setText(TextUtils.join("\n", details.getOpeningHours().getWeekdayText()));
                    }*/

                    ApiService api2 = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                    Call<BookPlaceInfo> call2 = api2.getBookPlaceInfo(place_id);
                    call2.enqueue(new Callback<BookPlaceInfo>() {
                        @Override
                        public void onResponse(Call<BookPlaceInfo> call, Response<BookPlaceInfo> response) {
                            if(response.isSuccessful()){
                                placeMenuProgress.setVisibility(View.GONE);
                                book_place_info = response.body();
                                if(book_place_info==null){
                                    menuUnav.setVisibility(View.VISIBLE);
                                }else{
                                    menuRecyclerView.setVisibility(View.VISIBLE);
                                    MenuAdapter mAdapter = new MenuAdapter(book_place_info.getMenu());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    menuRecyclerView.setLayoutManager(mLayoutManager);
                                    menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    menuRecyclerView.setAdapter(mAdapter);
                                }
                            }
                            Log.d("TAG onResponse", "getBookPlaceInfo: "+response.message());
                        }

                        @Override
                        public void onFailure(Call<BookPlaceInfo> call, Throwable t) {
                            Log.d("TAG onFailure", "getBookPlaceInfo: "+t.toString());
                        }
                    });
                }else{
                    try {
                        Log.d("TheError", response.errorBody().string());
                        Log.d("TheError 2", response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("TAG", "onResponse:getRestaurantDetails "+ response.toString());
            }

            @Override
            public void onFailure(Call<PlaceInfo> call, Throwable t) {
                Log.d("TheError onFailure", t.getMessage());
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
