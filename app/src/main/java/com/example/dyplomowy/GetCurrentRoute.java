package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

public class GetCurrentRoute extends AppCompatActivity {

    private final String TAG = "GetLocation";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LatLng currentLoc;
    private RoutesBase routes;
    private Poly poly;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for(Location location: locationResult.getLocations()) {
                currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        routes = intent.getParcelableExtra("routesKey");
        Log.d("currentTag", String.valueOf(routes.routesMap.keySet()));
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            checkSettingsAndStartLocUpdate();
            getLastLocation(this);
        } else {
//            askLocationPermission();
            Log.d("noPermission", "This should not have happened");
        }
    }

    private void checkSettingsAndStartLocUpdate(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> startLocUpdate());
        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(GetCurrentRoute.this, 1001);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocUpdate(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void getLastLocation(Context context){
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(location -> {
            if (location != null) {

                Thread thread1 = new Thread(() -> currentLoc =
                        new LatLng(location.getLatitude(), location.getLongitude()));

                thread1.start();
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    Log.d("threadFail", "something's not yes");
                    e.printStackTrace();
                }

                Log.d("aktualnaLokacja", String.valueOf(currentLoc));
                Thread thread2 = new Thread(() -> poly =
                        new Poly(routes, currentLoc));

                thread2.start();
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    Log.d("threadFail", "something's not yes");
                    e.printStackTrace();
                }
                Log.d("aktualnePoly", poly.currentRouteName);


                if (poly.currentRoute != null) {
                    Log.d(TAG, poly.currentRouteName);
                    Intent getMap = new Intent(context, FinalActivity.class);
                    getMap.putExtra("currentRoute", poly);
                    startActivity(getMap);
                }
                else if (poly.currentRouteName.equals("placeholder")){
                    Intent getMap = new Intent(context, EmptyFinalActivity.class);
                    getMap.putExtra("currentLoc", currentLoc);
                    startActivity(getMap);
                }
            }
            else  {
                getLastLocation(GetCurrentRoute.this);
                Log.d(TAG, "onSuccess: Location was null...");
            }
        });

        locationTask.addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage() ));
    }


}