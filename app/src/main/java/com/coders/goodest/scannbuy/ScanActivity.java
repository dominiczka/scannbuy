package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.goodest.scannbuy.barcode.BarcodeCaptureActivity;
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

public class ScanActivity extends AppCompatActivity {

    TextView mShopIdTextView;
    Button mScanButton;
    Button mCartButton;
    Button mAddToCartButton;
    Button mBackButton;
    ScanFragment scanFragment;
    private static final int RC_BARCODE_CAPTURE = 9001; //czy to jest potrzebne?

    ArrayList<Product> productsInCart;
    private static final String PRODUCT_QUERY_URL = "http://scanandbuy.000webhostapp.com/api/get.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        productsInCart = new ArrayList<Product>();
        for(int i=0;i<2;i++)
        {
            boolean exists = false;
            Product tmp = new Product("test", "fajki","lm", 15,100,"slodycze","Pychota",3,"1");
            tmp.setImage(R.drawable.lm);
            for(Product object : productsInCart)
            {
                if(object.getId_kod_kreskowy() == tmp.getId_kod_kreskowy())
                {
                    object.dodano_do_koszyka();
                    exists=true;
                    break;
                }
            }
            if(!exists)
            {
                tmp.dodano_do_koszyka();
                productsInCart.add(tmp);
            }
        }
        mShopIdTextView = findViewById(R.id.shopIdTextView);
        mBackButton = findViewById(R.id.back_button);

        scanFragment = new ScanFragment();
        setListener(scanFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.scanFragmentContainerScanActivity, scanFragment).commitNow();
        hideScanFrangment();

        mAddToCartButton = findViewById(R.id.add_to_cart_button);

        mScanButton = findViewById(R.id.scanProductButton);
        mScanButton.setVisibility(View.VISIBLE);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        mCartButton = findViewById(R.id.cartButton);
        mCartButton.setVisibility(View.VISIBLE);
        mCartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = new Bundle();
                //      extras.putSerializable("objects", scannedProductList);
                extras.putSerializable("cart", productsInCart);
                Intent intent = new Intent(ScanActivity.this, CartActivity.class);
                intent.putExtras(extras);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

//        fragmentButton = findViewById(R.id.fragment);
//        fragmentButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //showHide();
//
//            }});
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
                                boolean exists = false;
                                mCartButton.setVisibility(View.INVISIBLE);
                                mScanButton.setVisibility(View.INVISIBLE);
                                mAddToCartButton.setVisibility(View.VISIBLE);
                                mBackButton.setVisibility(View.VISIBLE);
                                for(final Product product : productsInCart)
                                {
                                    if(product.getId_kod_kreskowy().equals(newProduct.optString("ID_KOD_KRESKOWY")))
                                    {
                                        showScanFrangment();
                                        //   listener.onUpdateView(args);
                                        ///   productsInCart.add(scannedProduct);


                                        Bundle args = new Bundle();
                                        Log.i("produkt do frag: ", product.getNazwa());
                                        args.putFloat("cena", product.getCena());
                                        args.putString("nazwa",product.getNazwa());
                                        args.putString("opis",product.getOpis());

                                        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                product.dodano_do_koszyka();
                                                Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"), Toast.LENGTH_SHORT).show();
                                                hideScanFrangment();
                                                hideFragment();
                                            }
                                        });

                                        mBackButton.setVisibility(View.VISIBLE);
                                        mBackButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hideScanFrangment();
                                                hideFragment();
                                            }
                                        });
                                        scanFragment.setArguments(args);
                                        listener.onUpdateView(args);

                                        exists = true;
                                        break;
                                    }
                                }
                                if(!exists){
                                    final Product scannedProduct = new Product(
                                            newProduct.optString("ID_KOD_KRESKOWY"),
                                            newProduct.optString("NAZWA"),
                                            newProduct.optString("PRODUCENT"),
                                            Float.parseFloat(newProduct.optString("CENA")),
                                            Float.parseFloat(newProduct.optString("WAGA_GRAMY")),
                                            newProduct.optString("KATEGORIA"),
                                            newProduct.optString("OPIS"),
                                            Integer.parseInt(newProduct.optString("ILOSC_NA_STANIE")),
                                            newProduct.optString("ID_SKLEPU"));




                                    Bundle args = new Bundle();
                                    Log.i("produkt do frag: ", scannedProduct.getNazwa());
                                    args.putFloat("cena", scannedProduct.getCena());
                                    args.putString("nazwa",scannedProduct.getNazwa());
                                    args.putString("opis",scannedProduct.getOpis());


                                    showScanFrangment();

                                    mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addProduct(scannedProduct);
                                            Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"), Toast.LENGTH_SHORT).show();
                                            hideScanFrangment();
                                            hideFragment();
                                        }
                                    });

                                    mBackButton.setVisibility(View.VISIBLE);
                                    mBackButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            hideScanFrangment();
                                            hideFragment();
                                        }
                                    });
                                    scanFragment.setArguments(args);
                                    listener.onUpdateView(args);

                                    Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"), Toast.LENGTH_SHORT).show();
                                    Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getNazwa());
                                    Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getOpis());}

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

    public void addProduct (Product product){
        product.dodano_do_koszyka();
        productsInCart.add(product);
    }


    @Override
    public void onBackPressed(){
        if (scanFragment.isHidden()){
            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            hideScanFrangment();
            hideFragment();
        }
    }

    public void hideFragment (){
        mScanButton.setVisibility(View.VISIBLE);
        mCartButton.setVisibility(View.VISIBLE);
        mAddToCartButton.setVisibility(View.INVISIBLE);
        mBackButton.setVisibility(View.INVISIBLE);
    }
    public interface OnUpdateViewListener{
        public void onUpdateView(Bundle bundle);

    }

    private CartActivity.OnUpdateViewListener listener;

    public void setListener(CartActivity.OnUpdateViewListener listener) {
        this.listener = listener;
    }

    public void showScanFrangment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .show(scanFragment)
                .commit();
    }

    public void hideScanFrangment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(scanFragment)
                .commit();
    }

}