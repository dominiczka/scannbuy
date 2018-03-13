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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.goodest.scannbuy.barcode.BarcodeCaptureActivity;
import com.coders.goodest.scannbuy.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String QUERY_URL = "http://scanandbuy.000webhostapp.com/getShop.php";


    Button buttonStartShopping;
    LocationManager locationManager;
    LocationListener locationListener;
    Button mLocationButton;
    Button getshopAsyncBtn;
    Location loc;
    TextView coordinatesTV;
    // ArrayList<String> shopList;
    // String name;
    // Double lokalizacjaX, lokalizacjaY;
    //  String currentShop;

    // private static final String TAG = "BarcodeMain";
    // private static final int RC_BARCODE_CAPTURE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acquire a reference to the system Location Manager
        mLocationButton = findViewById(R.id.button2);
        coordinatesTV = findViewById(R.id.coordinatesTV);
        getshopAsyncBtn = findViewById(R.id.getshopAsyncBtn);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LOCATION",
                        "longitude" + location.getLongitude());
                Log.d("LOCATION",
                        "latitude" + location.getLatitude());
                //zapraszam do logcata po lokalizacje :)
                int shop;
                loc = location;
                coordinatesTV.setText("lat: " + location.getLatitude() + " | lon: " + location.getLongitude());
//                queryShop(String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()));
            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        //Added the condition needed to display the coordinates to the location in android <6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // taki sam if jest w metodzie onResume(), jeśli zmienimy coś tu, tam tez by wypadało
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




        getshopAsyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryShop("69","99");
                //queryShop(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()));
            }
        });


        buttonStartShopping = (Button) findViewById(R.id.buttonStartShopping);
        buttonStartShopping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                // launch barcode activity
//                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
//                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
//                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
//                startActivityForResult(intent, RC_BARCODE_CAPTURE);

                Intent intent = new Intent(MainActivity.this, ScanActivity.class);

                startActivity(intent);
            }
        });
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // taki sam if jest w metodzie onResume(), jeśli zmienimy coś tu, tam tez by wypadało
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    +                0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    +                0, locationListener);


        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (requestCode == RC_BARCODE_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    Intent intent = new Intent(MainActivity.this, ScanActivity.class);
//                    intent.putExtra("shopID", barcode.rawValue);
//                    startActivity(intent);
//                    finish();
//                }
//            } else {
//                Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        // first check for permissions
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
        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                Log.i("LOCATION","Your location: " + loc);
            }
        });
    }
    /*
        public void findCurrentLocation(Location location){
            for(String nazwa: shopList){
                if(location.getLongitude()== lokalizacjaY && location.getLatitude()==lokalizacjaX){
                    currentShop=nazwa;
                }}
        }
    */
    private void queryShop(String lattitude, String longitude) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
//        try {
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(QUERY_URL).append("?x=").append(lattitude).append("&y=").append(longitude);
        //urlString = URLEncoder.encode(urlStringBuilder.toString(), "UTF-8");
        Log.d("LOCATION","urlString = " + urlString + " | builder = " + urlStringBuilder.toString());
//        } catch (UnsupportedEncodingException e) {
//
//            // if this fails for some reason, let the user know why
//            e.printStackTrace();
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond
        Log.d("WEBSERVICE","calling: " + urlStringBuilder.toString());
        try{
            client.get(urlStringBuilder.toString(),
                    new JsonHttpResponseHandler() {

                        @Override
                        protected Object parseResponse(String responseBody) throws JSONException {
                            if (null == responseBody)
                                return null;
                            Object result = null;
                            //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
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

                            // 8. For now, just log results
                            Log.d("omg android", jsonObject.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                            // Display a "Toast" message
                            // to announce the failure
                            Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                            // Log error message
                            // to help solve any problems
                            Log.e("omg android", statusCode + " " + throwable.getMessage());
                        }
                    });
        }catch (Exception e) {
            Log.e("WS", "ERROR: ", e);
        }

    }

}