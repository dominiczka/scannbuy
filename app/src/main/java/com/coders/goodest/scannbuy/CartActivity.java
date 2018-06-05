package com.coders.goodest.scannbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coders.goodest.scannbuy.fragments.ScanFragment;
import com.coders.goodest.scannbuy.models.Product;
import com.coders.goodest.scannbuy.models.ProductAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ListView mCartProductsListView;
    TextView mSummaryTextView;
    ScanFragment scanFragment;
    Button mCheckoutButton;

    ProductAdapter productAdapter;

    ArrayList<Product> productsInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartProductsListView = findViewById(R.id.cartProductListView);
        scanFragment = new ScanFragment();
        setListener(scanFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.scanFragmentContainer, scanFragment).commitNow();
        hideScanFragment();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                productsInCart = null;
            } else {
                productsInCart = (ArrayList<Product>) extras.getSerializable("cart");
            }
        }
        else {
            productsInCart = (ArrayList<Product>) savedInstanceState.getSerializable("cart");
        }

        mSummaryTextView = findViewById(R.id.cartSummary);
        calculateSumToPay();

        productAdapter = new ProductAdapter(this, R.layout.product_cart_row_with_image, productsInCart);
        mCartProductsListView.setAdapter(productAdapter);

        mCartProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle args = new Bundle();
                Log.i("produkt do frag", productsInCart.get(position).getNazwa());
                args.putFloat("cena", productsInCart.get(position).getCena());
                args.putString("nazwa",productsInCart.get(position).getNazwa());
                args.putString("opis",productsInCart.get(position).getOpis());
                args.putString("id", productsInCart.get(position).getId_kod_kreskowy());
                args.putString("iloscKoszyk", productsInCart.get(position).getIlosc_w_koszyku()+"");
                args.putInt("iloscStan", productsInCart.get(position).getIlosc_na_stanie());

                args.putString("entryPoint", "cartList");

                showScanFragment();
                listener.onUpdateView(args);
            }

        });

        mCheckoutButton = (Button) findViewById(R.id.buttonFinalise);
        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float summary = calculateSumToPay();
                String sumString = Float.toString(summary);
                Log.d("SUM",
                        "sum: " + sumString);
//                if (sumString.equals(0)){
//                    Toast.makeText(getApplicationContext(), "Cart cannot be empty", Toast.LENGTH_SHORT);
//                }
              //  else {

                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putExtra( "sum", sumString);
                    startActivity(intent);
                    finish();
             //   }
            }
        });
    }

    public float calculateSumToPay(){

        float toPay = 0;

        for(Product object : productsInCart)
        {
            toPay += object.getCena() * object.getIlosc_w_koszyku();
        }

        toPay = round(toPay, 2);

        mSummaryTextView.setText("Total: " + Float.toString(toPay) + " zł");
        return  toPay;

    }

    public void showScanFragment(){

        mCartProductsListView.setVisibility(View.INVISIBLE);
        mSummaryTextView.setVisibility(View.INVISIBLE);

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

    @Override
    public void onBackPressed(){

        if (scanFragment.isHidden()){
            Intent intent = new Intent(CartActivity.this, ScanActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("cart", productsInCart);
            intent.putExtras(extras);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else {
            hideScanFragment();
            hideFragment();
        }

    }

    public void hideFragment (){

        mCartProductsListView.setVisibility(View.VISIBLE);
        mSummaryTextView.setVisibility(View.VISIBLE);

    }

    private OnUpdateViewListener listener;

    public interface OnUpdateViewListener{

        public void onUpdateView(Bundle bundle);

    }

    public void setListener(OnUpdateViewListener listener) {
        this.listener = listener;
    }

    public void deleteProductFromCart(String productId){

        onBackPressed();

        List<Object> toRemove = new ArrayList<Object>();
        for(Product product : productsInCart){
            if(product.getId_kod_kreskowy().equals(productId)){
                toRemove.add(product);
            }
        }
        productsInCart.removeAll(toRemove);

        calculateSumToPay();

        productAdapter.notifyDataSetChanged();

    }

    public void changeProductQuantity(String productId, int quantityNew){

        for(Product product : productsInCart){
            if(product.getId_kod_kreskowy().equals(productId)){
                product.setIlosc_w_koszyku(quantityNew);
            }
        }

        calculateSumToPay();

        productAdapter.notifyDataSetChanged();

    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}