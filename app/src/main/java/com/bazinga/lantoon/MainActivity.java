package com.bazinga.lantoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.bazinga.lantoon.registration.langselection.view.LangSelectionActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    CommonFunction cf;
    private FusedLocationProviderClient fusedLocationClient;
    private int locationPermissionCode = 2;
    String strDeviceId = "", strCurrentLoaction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstart);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        getCurrentLocation();
        strDeviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewLogo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button getstartCreateAccBtn = findViewById(R.id.getstartCreateAccBtn);
        getstartCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent langSelectionIntent = new Intent(MainActivity.this, LangSelectionActivity.class);
                langSelectionIntent.putExtra(Utils.TAG_DEVICE_ID, strDeviceId);
                langSelectionIntent.putExtra(Utils.TAG_CURRENT_LOCATION, strCurrentLoaction);
                startActivity(langSelectionIntent);
            }
        });

        Button getstartSigninBtn = findViewById(R.id.getstartSigninBtn);
        getstartSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                loginIntent.putExtra(Utils.TAG_DEVICE_ID, strDeviceId);
                loginIntent.putExtra(Utils.TAG_CURRENT_LOCATION, strCurrentLoaction);
                startActivity(loginIntent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationPermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        String[] aryPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Location permission not granded");
            ActivityCompat.requestPermissions(this, aryPermissions, locationPermissionCode);
            return;
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                strCurrentLoaction = String.valueOf(location.getLatitude() + "," + location.getLongitude());
                                System.out.println("Location " + strCurrentLoaction);
                            } else {

                            }
                        }
                    });
        }

    }
}