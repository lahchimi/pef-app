package com.aymsou.rstaurantsapp.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aymsou.rstaurantsapp.CatRestaurrants;
import com.aymsou.rstaurantsapp.PlaceDetails;
import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.adapters.RestaurantsAdapter;
import com.aymsou.rstaurantsapp.model.MapPlace;
import com.aymsou.rstaurantsapp.model.MapResults;
import com.aymsou.rstaurantsapp.model.Restaurant;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.LastLocationM;
import com.aymsou.rstaurantsapp.utils.Permission;
import com.aymsou.rstaurantsapp.utils.RetroClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



//GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
public class MapsActivity extends Fragment implements  LocationListener , OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    GoogleMap googleMap;
    Permission permissions;
    GoogleApi mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    boolean iSFirst = true;
    ArrayList<MapPlace> placeList;
    List<String> foundPlaces;
    SupportMapFragment supportMapFragment;
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    class MarkerData {
        public Float Rating;
        public String PlaceId;

        public MarkerData(Float rating, String placeId) {
            Rating = rating;
            PlaceId = placeId;
        }
    }

    class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        final View contentsView;

        InfoWindowAdapter() {
            contentsView = getActivity().getLayoutInflater().inflate(R.layout.map_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            Location marker_location = new Location("locA");
            marker_location.setLatitude(marker.getPosition().latitude);
            marker_location.setLongitude(marker.getPosition().longitude);

            TextView placeName = (TextView) contentsView.findViewById(R.id.placeName);
            placeName.setText(marker.getTitle());
            TextView placeDistance = (TextView) contentsView.findViewById(R.id.placeDistance);
            placeDistance.setText(MapPlace.getDistance(marker_location, mLastLocation));
            RatingBar ratingBar = (RatingBar) contentsView.findViewById(R.id.ratingBar);
            ratingBar.setRating(((MarkerData) marker.getTag()).Rating);
            Log.d("TAG xxx", "getInfoWindow: ");
            return contentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//       /* mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
//        builder.setAlwaysShow(true);
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//
//                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapFragment.this);
//                            return;
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        try {
//                            status.startResolutionForResult(getActivity(), 1000);
//                        } catch (IntentSender.SendIntentException e) {
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        break;
//                }
//            }
//        });*/
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }

    void getNearbyRestaurants(LatLng latLng) {


        ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);

        Call<List<Restaurant>> call = api.getCategoryRestaurants("");
        Log.d("CatRestaurrants xxx", call.toString() );
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()){

                    List<Restaurant> restaurants = response.body();
                    googleMap.clear();
                    for (int i=0; i<restaurants.size(); i++){


                        Marker markerToAdd = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng( restaurants.get(i).getLat(),restaurants.get(i).getLng() ))
                                .title( restaurants.get(i).getName())
                        );

                        markerToAdd.setTag(restaurants.get(i).getId());
                        markerToAdd.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_icon));
                        // Set a listener for marker click.
                        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) MapsActivity.this);
                        Log.d("onCreate.loop", "restaurants.LatLng: "+restaurants.get(i).getLat()+" , "+restaurants.get(i).getLng());
                    }


                }

                Log.d("onCreate resp xxx", response.toString() );
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.d("onCreate fail: ", t.getMessage());
            }
        });
     // ApiService api = RetroClient.getApiService(RetroClient.GMAPS_ROOT_URL);
     //   Call<MapResults> call = api.getNearbyRestaurants(String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
     //   call.enqueue(new Callback<MapResults>() {
     //       @Override
     //       public void onResponse(Call<MapResults> call, Response<MapResults> response) {
     //           if (response.isSuccessful()) {
     //               placeList = response.body().getResults();
     //               for (MapPlace place : placeList) {
     //                   Log.d("TAG xxx foundPlaces", "onResponse: MapPlace = " + place.toString());
     //                   if (!foundPlaces.contains(place.toString())) {
     //                       foundPlaces.add(place.toString());
     //                       Log.d("TAG foundPlaces", "onResponse: place = "+place.toString());
     //                       Marker markerToAdd = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng())).title(place.getName()));
     //                       markerToAdd.setTag(new MarkerData(place.getRating(), place.getPlace_id()));
     //                       markerToAdd.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_icon));
     //                   }
     //               }

     //               Log.d("TAG xxx", "onResponse:isSuccessful " + " message: " + response.body().getResults());
     //           }

     //           Log.d("TAG xxx", "onResponse:getNearbyRestaurants " + response.toString() + " message: " + response.message());
     //       }

     //       @Override
     //       public void onFailure(Call<MapResults> call, Throwable t) {

     //       }
     //   });
    }
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer tagid = (Integer) marker.getTag();

        Toast.makeText(getActivity(), marker.getTitle() +" has been clicked " , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), PlaceDetails.class);
        intent.putExtra("place_id", String.valueOf(tagid) );
        intent.putExtra("restaurant_id", tagid);
        intent.putExtra("current_location", LastLocationM.lastLocation);
        startActivity(intent);
        return true;
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LastLocationM.lastLocation = mLastLocation;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (iSFirst) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
            iSFirst = false;
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
       // googleMap.setMaxZoomPreference(20.0f);
        getNearbyRestaurants(latLng);

       // if (mGoogleApiClient != null) {
       //     LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
       // }
    }

    public MapsActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = new Permission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        placeList = new ArrayList<>();
        foundPlaces = new ArrayList<String>();
        Log.d("TAG xxx", "onCreate:permissions = " + permissions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
//        mMapView = (Fragment) rootView.findViewById(R.id.map);
//        mMapView.onCreate(savedInstanceState);
//        mMapView.onResume();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
               googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                   @Override
                   public void onMapClick(LatLng latLng) {
                       Log.d("TAG xxx", "onMapClick:LatLng = " + latLng );
                   }
               });
            }
        });
        supportMapFragment.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (permissions.checkPermissions())
            prepareMap();
        else
            permissions.requestPermissions(1);
        return rootView;
    }

    void prepareMap() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                buildGoogleApiClient();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    googleMap.setMyLocationEnabled(true);
                  //  return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setInfoWindowAdapter(new InfoWindowAdapter());
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        getNearbyRestaurants(googleMap.getCameraPosition().target);
                    }
                });
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(getActivity(), PlaceDetails.class);
                        intent.putExtra("place_id", ((MarkerData)marker.getTag()).PlaceId);
                        intent.putExtra("current_location", mLastLocation);
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
     /*   mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

      */
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        if (permissions.areAllRequiredPermissionsGranted(perms, grantResults))
            prepareMap();
        else
            permissions.showTipDialog(getActivity(), 1);
    }

}