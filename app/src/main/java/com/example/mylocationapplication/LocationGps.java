package com.example.mylocationapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Locale;

public class LocationGps {

    public static Double latitude, longitude;
    private final Activity activity;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    EditText street;
    public final boolean locationAccessGranted;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            LocationGps.latitude = location.getLatitude();
            LocationGps.longitude = location.getLongitude();
            try {
                street.setText(new Geocoder(activity, Locale.getDefault()).getFromLocation(LocationGps.latitude, LocationGps.longitude, 1).get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };;


    public LocationGps(Activity activity, EditText street) {
        this.activity = activity;
        this.street = street;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        //Only needed if you want to set an interval
      /*  locationRequest.setInterval(40000);
       locationRequest.setFastestInterval(2000);
       */
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationAccessGranted = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    /***
     * the method stops to request new location updates
     */
    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /***
     * the method starts to request Location updates after clicking on location button
     */
    @SuppressLint("MissingPermission") //locationAccessGranted handles PermissionRequest
    public void startLocationUpdates() {
        if (!locationAccessGranted) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateLocation();
    }

    /***
     * the method updates our old location with our current location
     */
    @SuppressLint("MissingPermission") //locationAccessGranted handles PermissionRequest
    public void updateLocation() {
    }

    /***
     * the method shows Gps Dialog if gps is disabled
     */
    public void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Enable Gps")
                .setCancelable(false)
                .setPositiveButton("Go to gps settings",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}