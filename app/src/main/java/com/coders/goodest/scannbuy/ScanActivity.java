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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity {

    TextView mShopIdTextView;
    Button mScanButton;
    Button mCartButton;
    Button mAddToCartButton;
    Button mBackButton;
    ScanFragment scanFragment;

    ArrayList<Product> productsInCart;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_CART_ACTIVITY = 9000;
    private static final String PRODUCT_QUERY_URL = "http://scanandbuy.000webhostapp.com/api/get.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        productsInCart = new ArrayList<Product>();

//        Product tmp = new Product("3333333333333", "fajki","lm", 15,100,"slodycze","Pychota",10,"1");
//        tmp.setImage(R.drawable.lm);
//        tmp.setIlosc_w_koszyku(6);
//        productsInCart.add(tmp);
//
//        Product tmp2 = new Product("1111111111111", "baton","snickers", 2.50F,60,"slodycze","takie dobre nawet",10,"1");
//        tmp2.setImage(R.drawable.lm);
//        tmp2.setIlosc_w_koszyku(4);
//        productsInCart.add(tmp2);
//
//        Product tmp3 = new Product("2222222222222", "gazeta","poranny", 1.90F,90,"papiernicze","newsy mocno",50,"1");
//        tmp3.setImage(R.drawable.lm);
//        tmp3.setIlosc_w_koszyku(2);
//        productsInCart.add(tmp3);

        mShopIdTextView = findViewById(R.id.shopIdTextView);
        mBackButton = findViewById(R.id.back_button);

        scanFragment = new ScanFragment();
        setListener(scanFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.scanFragmentContainerScanActivity, scanFragment).commitNow();
        hideScanFragment();

        mAddToCartButton = findViewById(R.id.add_to_cart_button);

        mScanButton = findViewById(R.id.scanProductButton);
        mScanButton.setVisibility(View.VISIBLE);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putSerializable("cart", productsInCart);
                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtras(extras);
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
                extras.putSerializable("cart", productsInCart);
                Intent intent = new Intent(ScanActivity.this, CartActivity.class);
                intent.putExtras(extras);
                startActivityForResult(intent, RC_CART_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    queryProduct(barcode.rawValue);
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == RC_CART_ACTIVITY){
            productsInCart = (ArrayList<Product>) data.getExtras().getSerializable("cart");
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
                                if(newProduct.optString("NAZWA").isEmpty())
                                {
                                    Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                                    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                                    Toast.makeText(getApplicationContext(), new StringBuilder("Produktu nie ma w bazie."), Toast.LENGTH_SHORT).show();
                                }
                                boolean exists = false;

                                mCartButton.setVisibility(View.INVISIBLE);
                                mScanButton.setVisibility(View.INVISIBLE);

                                mAddToCartButton.setVisibility(View.VISIBLE);
                                mBackButton.setVisibility(View.VISIBLE);

                                for(final Product product : productsInCart)
                                {
                                    if(product.getId_kod_kreskowy().equals(newProduct.optString("ID_KOD_KRESKOWY")))
                                    {
                                        showScanFragment();

                                        Bundle args = new Bundle();
                                        Log.i("produkt do frag", product.getNazwa());
                                        args.putString("id", product.getId_kod_kreskowy());
                                        args.putFloat("cena", product.getCena());
                                        args.putString("nazwa",product.getNazwa());
                                        args.putString("opis",product.getOpis());
                                        args.putString("iloscKoszyk",product.getIlosc_w_koszyku()+"");
                                        args.putInt("iloscStan", product.getIlosc_na_stanie());

                                        args.putString("entryPoint", "scanProductOnList");

                                        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                product.dodano_do_koszyka();
                                                Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"),
                                                        Toast.LENGTH_SHORT).show();
                                                hideScanFragment();
                                                hideFragment();
                                            }
                                        });

                                        mBackButton.setVisibility(View.VISIBLE);
                                        mBackButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hideScanFragment();
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
                                    Log.i("produkt do frag", scannedProduct.getNazwa());
                                    args.putString("id", scannedProduct.getId_kod_kreskowy());
                                    args.putFloat("cena", scannedProduct.getCena());
                                    args.putString("nazwa",scannedProduct.getNazwa());
                                    args.putString("opis",scannedProduct.getOpis());
                                    args.putString("iloscKoszyk",scannedProduct.getIlosc_w_koszyku()+"");
                                    args.putInt("iloscStan", scannedProduct.getIlosc_na_stanie());

                                    args.putString("entryPoint", "scanNewProduct");

                                    productsInCart.add(scannedProduct);

                                    showScanFragment();

                                    mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplicationContext(), new StringBuilder("Dodano produkt ").append(productsInCart.get(productsInCart.size()-1).getNazwa()).append("!"),
                                                    Toast.LENGTH_SHORT).show();
                                            hideScanFragment();
                                            hideFragment();
                                        }
                                    });

                                    mBackButton.setVisibility(View.VISIBLE);
                                    mBackButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            List<Object> toRemove = new ArrayList<Object>();
                                            for(Product product : productsInCart){
                                                if(product.getId_kod_kreskowy().equals(scannedProduct.getId_kod_kreskowy())){
                                                    toRemove.add(product);
                                                }
                                            }
                                            productsInCart.removeAll(toRemove);

                                            hideScanFragment();
                                            hideFragment();
                                        }
                                    });

                                    scanFragment.setArguments(args);
                                    listener.onUpdateView(args);

                                    Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getNazwa());
                                    Log.d("queryProduct", productsInCart.get(productsInCart.size()-1).getOpis());
                                }
                            } catch (JSONException e) {
                                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                                Toast.makeText(getApplicationContext(), new StringBuilder("Produktu nie ma w bazie."), Toast.LENGTH_LONG).show();

                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                            Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                            startActivityForResult(intent, RC_BARCODE_CAPTURE);
                            Log.e("queryProduct", statusCode + " " + throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e("WS", "ERROR: ", e);
        }
    }

    @Override
    public void onBackPressed(){
        if (scanFragment.isHidden()){
            Toast.makeText(getApplicationContext(), "Your shopping progress will be lost. Do not leave if you do not want your progress to be gone!", Toast.LENGTH_SHORT);
            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            hideScanFragment();
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

    public void showScanFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .show(scanFragment)
                .commit();

    }

    public void hideScanFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(scanFragment)
                .commit();

    }

    public void changeProductQuantity(String productId, int quantityNew){

        for(Product product : productsInCart){
            if(product.getId_kod_kreskowy().equals(productId)){
                product.setIlosc_w_koszyku(quantityNew);
            }
        }

    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}