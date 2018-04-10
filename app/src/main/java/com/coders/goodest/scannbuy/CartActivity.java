package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.coders.goodest.scannbuy.fragments.ScanFragment;
import com.coders.goodest.scannbuy.models.Product;
import com.coders.goodest.scannbuy.models.ProductAdapter;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<Product> productsInCart;
    Product product;
    ListView mCartProductsListView;

    TextView mSummaryTextView;
    ScanFragment scanFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartProductsListView = findViewById(R.id.cartProductListView);
        scanFragment = new ScanFragment();
        setListener(scanFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.scanFragmentContainer, scanFragment).commitNow();
        hideScanFrangment();


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
        float toPay=0;
        for(Product object : productsInCart)
        {
            toPay+= object.getCena()*object.getIlosc_w_koszyku();
        }
        mSummaryTextView = findViewById(R.id.cartSummary);
        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_cart_row_with_image, productsInCart);
        mCartProductsListView.setAdapter(productAdapter);

        mCartProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle args = new Bundle();
                Log.i("produkt do frag: ", productsInCart.get(position).getNazwa());
                args.putFloat("cena", productsInCart.get(position).getCena());
                args.putString("nazwa",productsInCart.get(position).getNazwa());
                args.putString("opis",productsInCart.get(position).getOpis());

                showScanFrangment();
                listener.onUpdateView(args);
            }
        });
    }

    public void showScanFrangment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .show(scanFragment)
                .commit();
        mCartProductsListView.setVisibility(View.INVISIBLE);
        mSummaryTextView.setVisibility(View.INVISIBLE);
    }

    public void hideScanFrangment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(scanFragment)
                .commit();
        // mCartProductsListView.setVisibility(View.INVISIBLE);
        //mSummaryTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed(){
        if (scanFragment.isHidden()){
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            hideScanFrangment();
            hideFragment();
        }
    }

    public void hideFragment (){
        mCartProductsListView.setVisibility(View.VISIBLE);
        mSummaryTextView.setVisibility(View.VISIBLE);
    }

    public interface OnUpdateViewListener{
        public void onUpdateView(Bundle bundle);

    }
    private OnUpdateViewListener listener;

    public void setListener(OnUpdateViewListener listener) {
        this.listener = listener;
    }
}