package com.example.mylocationapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean buttonisChecked = false;
    private ActivityResultLauncher<String> automaticLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        EditText editText = findViewById(R.id.editTextTextPersonName);

        LocationGps locationGps = new LocationGps(MainActivity.this,editText);

        automaticLocationRequest = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        locationGps.updateLocation();
                    } else {
                        Toast.makeText(this, "WE NEED LOCATION", Toast.LENGTH_SHORT).show();
                    }
                });
        button.setText("OFF");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationGps.locationAccessGranted) {
                    if (buttonisChecked) {
                        buttonisChecked = false;
                        locationGps.stopLocationUpdates();
                        button.setText("OFF");
                    } else {
                        buttonisChecked = true;
                        button.setText("ON");
                        locationGps.startLocationUpdates();
                        textView.setText(String.valueOf(LocationGps.latitude));
                    }
                }
                else {
                    automaticLocationRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        });
    }
}