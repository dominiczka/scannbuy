package com.coders.goodest.scannbuy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class MainActivity extends AppCompatActivity {
    private static final String QUERY_URL = "http://scanandbuy.000webhostapp.com/api/getShop.php";


    Button buttonStartShopping;
    LocationManager locationManager;
    LocationListener locationListener;
   // Button mLocationButton;
   // Button getshopAsyncBtn;
    Location loc;
    //TextView coordinatesTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mLocationButton = findViewById(R.id.button2);
      //  coordinatesTV = findViewById(R.id.coordinatesTV);
       // getshopAsyncBtn = findViewById(R.id.getshopAsyncBtn);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LOCATION",
                        "longitude" + location.getLongitude());
                Log.d("LOCATION",
                        "latitude" + location.getLatitude());
                int shop;
                loc = location;
        //        coordinatesTV.setText("lat: " + location.getLatitude() + " | lon: " + location.getLongitude());
            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) { }

            @Override
            public void onProviderEnabled(String s) { }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };


        //Added the condition needed to display the coordinates to the location in android <6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    +                0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    +                0, locationListener);

            configure_button();
        }
        else{
            configure_button();
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        Log.i("LOCATION","Your location: " + loc);
        queryShop("69","99");
      /*  getshopAsyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryShop("69","99");
            }
        });*/


        buttonStartShopping = (Button) findViewById(R.id.buttonStartShopping);
        buttonStartShopping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    +                0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    +                0, locationListener);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }

    }

    void configure_button() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

    /*    mLocationButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                Log.i("LOCATION","Your location: " + loc);
            }
        });*/
    }

    private void queryShop(String lattitude, String longitude) {

        String urlString = "";
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(QUERY_URL).append("?x=").append(lattitude).append("&y=").append(longitude);

        Log.d("LOCATION","urlString = " + urlString + " | builder = " + urlStringBuilder.toString());
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("WEBSERVICE","calling: " + urlStringBuilder.toString());

        try {

            client.get(urlStringBuilder.toString(),
                    new JsonHttpResponseHandler() {

                        @Override
                        protected Object parseResponse(String responseBody) throws JSONException {
                            if (null == responseBody)
                                return null;
                            Object result = null;
                            String jsonString = responseBody.trim();
                            if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                                result = new JSONTokener(jsonString).nextValue();
                            }
                            if (result == null) {
                                result = jsonString;
                            }
                            Log.d("WS","response: " + result.toString());
                            Log.d("WS","response: " + ((JSONArray)result).toString());
                            return result;
                        }

                        @Override
                        public void onSuccess(JSONArray jsonObject) {
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                            Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            Log.e("WS", "ERROR: ", e);
        }

    }

}