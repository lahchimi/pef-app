package com.aymsou.rstaurantsapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.LastLocationM;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRestaurantReq extends AppCompatActivity implements  OnMapReadyCallback {

    MapView mapView;
    Marker marker;
    String dragLat, dragLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_req);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

/*
        mapView = (MapView) findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapView.onResume();
            }
        });
     //   mapView.onCreate(savedInstanceState);


        mapView.onResume();
       try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng latLng = new LatLng(LastLocationM.lastLocation.getLatitude(), LastLocationM.lastLocation.getLongitude());
                Location marker_location = new Location("locA");
                marker_location.setLatitude(latLng.latitude);
                marker_location.setLongitude(latLng.longitude);
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                marker = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                });
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng dragPosition = marker.getPosition();
                        dragLat = String.valueOf(dragPosition.latitude);
                        dragLong = String.valueOf(dragPosition.longitude);
                    }
                });
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        dragLat = String.valueOf(latLng.latitude);
                        dragLong = String.valueOf(latLng.longitude);
                        marker.setPosition(latLng);
                    }
                });
            }
        }); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId() == R.id.action_add_rest){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddRestaurantReq.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_rest_form, null);
            final EditText inputEmail = (EditText)dialogView.findViewById(R.id.inputEmail);
            final EditText inputRestName = (EditText)dialogView.findViewById(R.id.inputRestName);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle(getResources().getString(R.string.title_activity_add_restaurant_req));
            dialogBuilder.setCancelable(false);
            dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.setPositiveButton(getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(inputEmail.getText().toString().trim().isEmpty() || inputRestName.getText().toString().trim().isEmpty())
                        return;
                    final ProgressDialog progress = new ProgressDialog(AddRestaurantReq.this);
                    progress.setMessage(getResources().getString(R.string.please_wait));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                Log.d("AddRestaurantReq abdel:", "add_restaurant:"+RetroClient.WEBSITE_ROOT_URL);

                Call<String> call = api.sendRestaurantRequest(dragLat, dragLong, inputEmail.getText().toString(), inputRestName.getText().toString());

                   call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(AddRestaurantReq.this, getResources().getString(R.string.rest_req_sent_succcess), Toast.LENGTH_LONG).show();
                            }
                            Log.d("AddRestauReq response:", response.toString());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("AddRestauReq onFailure:", t.toString());
                            progress.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
       // mapView.onResume();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mapView.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
