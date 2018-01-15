package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.goodest.scannbuy.barcode.BarcodeCaptureActivity;
import com.coders.goodest.scannbuy.R;
import com.coders.goodest.scannbuy.fragments.ScanFragment;
import com.coders.goodest.scannbuy.models.Product;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ScanActivity extends AppCompatActivity {

    TextView mShopIdTextView;
    Button mScanButton;
    Button mCartButton;
    Button fragmentButton;
    private static final int RC_BARCODE_CAPTURE = 9001; //czy to jest potrzebne?

    ArrayList<Product> productsInCart;
    private static final String PRODUCT_QUERY_URL = "http://scanandbuy.000webhostapp.com/get.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        productsInCart = new ArrayList<Product>();

        mShopIdTextView = findViewById(R.id.shopIdTextView);

        mScanButton = findViewById(R.id.scanProductButton);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        mCartButton = findViewById(R.id.cartButton);
        mCartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = new Bundle();
          //      extras.putSerializable("objects", scannedProductList);
                extras.putSerializable("cart", productsInCart);
                Intent intent = new Intent(ScanActivity.this, CartActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        fragmentButton = findViewById(R.id.fragment);
        fragmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showHide();

            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(getApplicationContext(), barcode.rawValue, Toast.LENGTH_SHORT).show();
                    queryProduct(barcode.rawValue);
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void queryProduct(String barcode) {

        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(PRODUCT_QUERY_URL).append(barcode);
        AsyncHttpClient client = new AsyncHttpClient();

        try{
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
                            return result;
                        }

                        @Override
                        public void onSuccess(JSONArray jsonObject) {
                            try {
                                JSONObject newProduct = jsonObject.getJSONObject(0);
                                //addToCart(newProduct);
                                Product scannedProduct = new Product(
                                        newProduct.optString("ID_KOD_KRESKOWY"),
                                        newProduct.optString("NAZWA"),
                                        newProduct.optString("PRODUCENT"),
                                        Float.parseFloat(newProduct.optString("CENA")),
                                        Float.parseFloat(newProduct.optString("WAGA_GRAMY")),
                                        newProduct.optString("KATEGORIA"),
                                        newProduct.optString("OPIS"),
                                        Integer.parseInt(newProduct.optString("ILOSC_NA_STANIE")),
                                        newProduct.optString("ID_SKLEPU"));
                                productsInCart.add(scannedProduct);
                                Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"), Toast.LENGTH_SHORT).show();
                                Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getNazwa());
                                Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getOpis());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                            Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("queryProduct", statusCode + " " + throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e("WS", "ERROR: ", e);
        }
    }

   public void showHide(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //find the fragment by View or Tag
        ScanFragment myFrag = (ScanFragment) fragmentManager.findFragmentById(R.id.scanFragment);
        if(myFrag.isHidden())
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .show(myFrag)
                .commit();
        else
            fragmentManager.beginTransaction()
               .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
               .hide(myFrag)
               .commit();
    }

}
