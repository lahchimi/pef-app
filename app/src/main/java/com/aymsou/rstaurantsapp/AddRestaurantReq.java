package com.aymsou.rstaurantsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aymsou.rstaurantsapp.fragments.MapsActivity;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.LastLocationM;
import com.aymsou.rstaurantsapp.utils.Permission;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class AddRestaurantReq extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    MapView mapView;
    Marker marker;
    Permission permissions;
    String dragLat, dragLong;

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

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


        mapView = (MapView) findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
            Log.d("TAG", "onCreate: MapsInitializer initialize");
        } catch (Exception e) {
            e.printStackTrace();
        }
        verifyPermissions();
        prepareMap();
    }

    void prepareMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {



               //LatLng latLng = new LatLng(LastLocationM.lastLocation.getLatitude(), LastLocationM.lastLocation.getLongitude());
               LatLng latLng = new LatLng(34.26598, -6.58893);
                dragLat = String.valueOf(34.26598);
                dragLong = String.valueOf(-6.58893);

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
                        Log.d("TAG onMapClick", "onMapClick: "+latLng);
                    }
                });
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        Log.d("TAG onMarkerDragStart", "onMarkerDragStart: "+marker);
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        Log.d("TAG onMarkerDrag", "onMarkerDrag: "+marker);
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng dragPosition = marker.getPosition();
                        dragLat = String.valueOf(dragPosition.latitude);
                        dragLong = String.valueOf(dragPosition.longitude);
                        Log.d("TAG onMarkerDragEnd", "onMarkerDragEnd: dragLat="+dragLat+" dragLong="+dragLong);
                    }
                });
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        dragLat = String.valueOf(latLng.latitude);
                        dragLong = String.valueOf(latLng.longitude);
                        marker.setPosition(latLng);
                        Log.d("TAG onMapLongClick", "onMapLongClick: dragLat="+dragLat+" dragLong="+dragLong);
                    }
                });
            }
        });
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
            final EditText inputRestPhone = (EditText)dialogView.findViewById(R.id.inputRestPhone);
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
                    if(inputEmail.getText().toString().trim().isEmpty() ||
                            inputRestName.getText().toString().trim().isEmpty() ||
                            inputRestPhone.getText().toString().trim().isEmpty())
                        return;
                    Log.d("TAG", "onClick: +dragLat="+dragLat +" dragLong = "+dragLong);
                    if( dragLat.trim().isEmpty() ){
                        Toast.makeText(AddRestaurantReq.this, "Please select on map ", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    final ProgressDialog progress = new ProgressDialog(AddRestaurantReq.this);
                    progress.setMessage(getResources().getString(R.string.please_wait));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
                    Log.d("TAG", "onClick:action_add_rest ");

                    Call<String> call = api.sendRestaurantRequest(
                            String.valueOf(dragLong),
                            String.valueOf(dragLat) ,
                            inputEmail.getText().toString(),
                            inputRestName.getText().toString(),
                            inputRestPhone.getText().toString()
                    );

                   call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(AddRestaurantReq.this, getResources().getString(R.string.rest_req_sent_succcess), Toast.LENGTH_LONG).show();
                            }
                            Log.d("AddRestauReq response:", String.valueOf(response)
                            );
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("AddRestauReq onFailure:"," " + call);
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
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);

        if (permissions.areAllRequiredPermissionsGranted(perms, grantResults))
            prepareMap();
        else
            permissions.showTipDialog(this, 1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void verifyPermissions(){
        Log.d("AddRestaurantReq", "verifyPermissions: xx...");
        int permissionACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(AddRestaurantReq.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if( permissionACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                    AddRestaurantReq.this,
                    LOCATION_PERMISSIONS,
                    1
            );
        }
        int permissionACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(AddRestaurantReq.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if( permissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(
                    AddRestaurantReq.this,
                    LOCATION_PERMISSIONS,
                    1
            );
            Log.d("AddRestaurantReq", "verifyPermissions: if...");
        }
    }

}
