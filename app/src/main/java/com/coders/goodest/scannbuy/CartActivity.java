package com.coders.goodest.scannbuy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.coders.goodest.scannbuy.models.Product;
import com.coders.goodest.scannbuy.models.ProductAdapter;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<Product> productsInCart;
    ListView mCartProductsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartProductsListView = findViewById(R.id.cartProductListView);

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

        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_cart_row, productsInCart);
        mCartProductsListView.setAdapter(productAdapter);
    }
}
