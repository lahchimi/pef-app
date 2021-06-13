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
import android.widget.RatingBar;
import android.widget.TextView;

import com.aymsou.rstaurantsapp.PlaceDetails;
import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.model.MapPlace;
import com.aymsou.rstaurantsapp.model.MapResults;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapView mMapView;
    GoogleMap googleMap;
    Permission permissions;
    GoogleApi mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    boolean iSFirst = true;
    ArrayList<MapPlace> placeList;
    List<String> foundPlaces;

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

            return contentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /* mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapFragment.this);
                            return;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    void getNearbyRestaurants(LatLng latLng) {
        ApiService api = RetroClient.getApiService(RetroClient.GMAPS_ROOT_URL);
        Call<MapResults> call = api.getNearbyRestaurants(String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
        call.enqueue(new Callback<MapResults>() {
            @Override
            public void onResponse(Call<MapResults> call, Response<MapResults> response) {
                if (response.isSuccessful()) {
                    placeList = response.body().getResults();
                   /* for (MapPlace place : placeList) {
                        if (!foundPlaces.contains(place.toString())) {
                            foundPlaces.add(place.toString());
                            Log.d("TAG foundPlaces", "onResponse: "+place.toString());
                            Marker markerToAdd = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng())).title(place.getName()));
                            markerToAdd.setTag(new MarkerData(place.getRating(), place.getPlace_id()));
                            markerToAdd.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_icon));
                        }
                    }*/
                    Log.d("TAG", "onResponse: " + response.toString() + " message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MapResults> call, Throwable t) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
     /*   mLastLocation = location;
        LastLocationM.lastLocation = mLastLocation;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (iSFirst) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            iSFirst = false;
        }
        getNearbyRestaurants(latLng);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
    }

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* permissions = new Permission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        placeList = new ArrayList<>();
        foundPlaces = new ArrayList<String>();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
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
        mMapView.getMapAsync(new OnMapReadyCallback() {
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
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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