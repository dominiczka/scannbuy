package com.coders.goodest.scannbuy;

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
import android.widget.Toast;

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

    Button buttonPlus;
    Button buttonMinus;
    Button buttonDelete;
    TextView productQuantity;
    TextView productQuantityTextView;

    int quantityOfProduct;
    int currentPositionInCart;

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
        float toPay=0;
        for(Product object : productsInCart)
        {
            toPay+= object.getCena()*object.getIlosc_w_koszyku();
        }
        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");

        final ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_cart_row_with_image, productsInCart);
        mCartProductsListView.setAdapter(productAdapter);

        buttonPlus = findViewById(R.id.button_plus_cart);
        buttonMinus = findViewById(R.id.button_minus_cart);
        buttonDelete = findViewById(R.id.button_delete_cart);
        productQuantity = findViewById(R.id.product_quantity_cart);
        productQuantityTextView = findViewById(R.id.product_quantity_TextView_cart);

        mCartProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle args = new Bundle();
                Log.i("produkt do frag: ", productsInCart.get(position).getNazwa());
                args.putFloat("cena", productsInCart.get(position).getCena());
                args.putString("nazwa",productsInCart.get(position).getNazwa());
                args.putString("opis",productsInCart.get(position).getOpis());
                args.putString("id", productsInCart.get(position).getId_kod_kreskowy());

                currentPositionInCart = position;

                quantityOfProduct = productsInCart.get(position).getIlosc_w_koszyku();
                productQuantity.setText(quantityOfProduct+"");

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        productsInCart.remove(currentPositionInCart);
                        mCartProductsListView.invalidateViews();
                        onBackPressed();

                        float toPay=0;
                        for(Product object : productsInCart)
                        {
                            toPay+= object.getCena()*object.getIlosc_w_koszyku();
                        }
                        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");
                    }
                });

                buttonPlus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        quantityOfProduct++;

                        if(quantityOfProduct > productsInCart.get(currentPositionInCart).getIlosc_na_stanie()){
                            quantityOfProduct--;
                            Toast.makeText(getApplicationContext(), new StringBuilder("Na stanie znajduje sie ").append(productsInCart.get(currentPositionInCart).getIlosc_na_stanie()).append(" produktow."), Toast.LENGTH_SHORT).show();
                        }

                        productQuantity.setText(quantityOfProduct+"");
                        productsInCart.get(currentPositionInCart).setIlosc_w_koszyku(quantityOfProduct);
                        mCartProductsListView.invalidateViews();

                        float toPay=0;
                        for(Product object : productsInCart)
                        {
                            toPay+= object.getCena()*object.getIlosc_w_koszyku();
                        }
                        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");
                    }
                });

                buttonMinus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        quantityOfProduct--;

                        if(quantityOfProduct < 1)
                            quantityOfProduct++;

                        productQuantity.setText(quantityOfProduct+"");
                        productsInCart.get(currentPositionInCart).setIlosc_w_koszyku(quantityOfProduct);
                        mCartProductsListView.invalidateViews();

                        float toPay=0;
                        for(Product object : productsInCart)
                        {
                            toPay+= object.getCena()*object.getIlosc_w_koszyku();
                        }
                        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");
                    }
                });

                showScanFragment();
                listener.onUpdateView(args);
            }
        });
    }

    public void showScanFragment(){

        mCartProductsListView.setVisibility(View.INVISIBLE);
        mSummaryTextView.setVisibility(View.INVISIBLE);

        buttonMinus.setVisibility(View.VISIBLE);
        buttonPlus.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.VISIBLE);
        productQuantity.setVisibility(View.VISIBLE);
        productQuantityTextView.setVisibility(View.VISIBLE);

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
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            hideScanFragment();
            hideFragment();
        }
    }

    public void hideFragment (){
        mCartProductsListView.setVisibility(View.VISIBLE);
        mSummaryTextView.setVisibility(View.VISIBLE);

        buttonMinus.setVisibility(View.INVISIBLE);
        buttonPlus.setVisibility(View.INVISIBLE);
        buttonDelete.setVisibility(View.INVISIBLE);
        productQuantity.setVisibility(View.INVISIBLE);
        productQuantityTextView.setVisibility(View.INVISIBLE);
    }

    public interface OnUpdateViewListener{
        public void onUpdateView(Bundle bundle);

    }
    private OnUpdateViewListener listener;

    public void setListener(OnUpdateViewListener listener) {
        this.listener = listener;
    }
}