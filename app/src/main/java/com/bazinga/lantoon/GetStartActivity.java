package com.bazinga.lantoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.bazinga.lantoon.registration.langselection.view.LangSelectionActivity;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetStartActivity extends AppCompatActivity {

    CommonFunction cf;
    private FusedLocationProviderClient fusedLocationClient;
    private int locationPermissionCode = 2;
    String strDeviceId = "", strCurrentLoaction = "", strRegionCode = "";
    SessionManager sessionManager;
    List<Address> addresses;
    Geocoder geocoder;
    Button getstartCreateAccBtn;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstart);
        sessionManager = new SessionManager(this);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());

        getCurrentLocation();
        geocoder = new Geocoder(this);
        strDeviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        System.out.println("ga deviceid " + strDeviceId);
        System.out.println("ga countryCode " + getCountryCode(this));
        ImageView imageView = (ImageView) findViewById(R.id.imageViewLogo);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
              /*  if (ApiClient.BASE_URL.equals("http://bazinga.ai/"))
                    ApiClient.BASE_URL = "https://www.lantoon.net/";
                else if (ApiClient.BASE_URL.equals("https://www.lantoon.net/"))
                    ApiClient.BASE_URL = "http://bazinga.ai/";

                Toast.makeText(GetStartActivity.this, "Server Changed to "+ApiClient.BASE_URL, Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });
        getstartCreateAccBtn = findViewById(R.id.getstartCreateAccBtn);
        if (ApiClient.isTest) {
            getstartCreateAccBtn.setVisibility(View.VISIBLE);
            strRegionCode = "569";
        }
        getstartCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent langSelectionIntent = new Intent(GetStartActivity.this, LangSelectionActivity.class);
                langSelectionIntent.putExtra(Tags.TAG_DEVICE_ID, strDeviceId);
                langSelectionIntent.putExtra(Tags.TAG_CURRENT_LOCATION, strCurrentLoaction);
                langSelectionIntent.putExtra(Tags.TAG_REGION_CODE, strRegionCode);
                langSelectionIntent.putExtra(Tags.TAG_NOTIFICATION_TOKEN, getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
                startActivity(langSelectionIntent);
            }
        });

        Button getstartSigninBtn = findViewById(R.id.getstartSigninBtn);
        getstartSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(GetStartActivity.this, LoginActivity.class);
                loginIntent.putExtra(Tags.TAG_NOTIFICATION_TOKEN, getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
                loginIntent.putExtra(Tags.TAG_DEVICE_ID, strDeviceId);
                loginIntent.putExtra(Tags.TAG_CURRENT_LOCATION, strCurrentLoaction);
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
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    //addresses = geocoder.getFromLocation(39.30,30.58, 1);//turky
                                    //addresses = geocoder.getFromLocation(48.76,33.78, 1);
                                    Address address = addresses.get(0);
                                    System.out.println("Location Address " + address.getCountryCode());
                                    if (address.getCountryCode().equals("IN") || address.getCountryCode().equals("TR")) {
                                        if (address.getCountryCode().equals("IN"))
                                            strRegionCode = "569";
                                        if (address.getCountryCode().equals("TR"))
                                            strRegionCode = "896";
                                        getstartCreateAccBtn.setVisibility(View.VISIBLE);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    });
        }

    }

    public static String getCountryCode(@Nullable Context context) {
        if (context != null) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String countryCode = telephonyManager.getNetworkCountryIso();
                if (!TextUtils.isEmpty(countryCode)) {
                    return toUpperInvariant(countryCode);
                }
            }
        }
        return toUpperInvariant(Locale.getDefault().getCountry());
    }

    private static String toUpperInvariant(String countryCode) {
        return countryCode;
    }
}