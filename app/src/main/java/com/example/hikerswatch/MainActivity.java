package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    double longitude= 0.0 , latitude = 0.0 , accuracy=0.0 , altitude = 0.0 ;
    TextView long_textview , latt_textview , acc_textview , altitude_textview , address_textview ;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long_textview = findViewById(R.id.longitude_textview);
        latt_textview = findViewById(R.id.latitude_textview);
        acc_textview = findViewById(R.id.accuracy_textview);
        altitude_textview = findViewById(R.id.altitude_textview);
        address_textview = findViewById(R.id.address_textview);
         progressBar = findViewById(R.id.progressBar);
         progressBar.setVisibility(View.VISIBLE);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("MY_LOCATION", "onLocationChanged: " + location);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                accuracy = location.getAccuracy();
                altitude = location.getAltitude();
                progressBar.setVisibility(View.INVISIBLE);
                LatLng MyLocation = new LatLng(latitude, longitude);
//                Log.d("Mylocation", "onMapReady: "+ latitude);
//                Log.d("Mylocation", "onMapReady: "+ longitude);
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude() , location.getLongitude() , 1);
                    if(list!=null && list.size()>0){
                        long_textview.setText(String.valueOf(list.get(0).getLongitude()).substring(0,9));
                        latt_textview.setText(String.valueOf(list.get(0).getLatitude()).substring(0,9));
                        address_textview.setText(list.get(0).getAddressLine(0));
                        if(String.valueOf(altitude).length()>9) {
                            altitude_textview.setText(String.valueOf(altitude).substring(0, 9));
                        }else{
                            altitude_textview.setText(String.valueOf(altitude));
                        }
                        if(String.valueOf(accuracy).length()>9) {
                            acc_textview.setText(String.valueOf(accuracy).substring(0,9));
                        }else{
                            acc_textview.setText(String.valueOf(accuracy));
                        }

//                        Log.d("MyLocation", "onLocationChanged: " + list.get(0).toString());
//                        Log.d("MyLocation", "onLocationChanged: " + list.get(0).getAddressLine(0));
//                        Log.d("MyLocation", "onLocationChanged: " + list.get(0).getAdminArea());
//                        Log.d("MyLocation", "onLocationChanged: " + list.get(0).getCountryName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, locationListener);
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10, locationListener);
            }
        }
    }


}