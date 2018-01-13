package com.coders.goodest.scannbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.coders.goodest.scannbuy.R;
import com.coders.goodest.scannbuy.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> cartProductList;
    ArrayList<Product> productsInCart;
    TextView mCartProductsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartProductsTextView = findViewById(R.id.cartProductTextView);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cartProductList = null;
                productsInCart = null;
            } else {
                cartProductList = (ArrayList<String>) extras.getSerializable("objects");
                productsInCart = (ArrayList<Product>) extras.getSerializable("cart");
            }
        }
        else {
            cartProductList = (ArrayList<String>) savedInstanceState.getSerializable("objects");
            productsInCart = (ArrayList<Product>) savedInstanceState.getSerializable("cart");
        }

        //TODO 4 wypisywanie bez powtorek z uwzględnieniem ilości
        for (Product product : productsInCart) {
            StringBuilder productText = new StringBuilder();
            productText.append(product.getNazwa()).append(", ").append(product.getProducent()).append("\n").append(product.getCena()).append("\n").append(product.getOpis()).append("\n").append("\n");
            mCartProductsTextView.append(productText);
        }
        /*
        for (String product : cartProductList) {
            mCartProductsTextView.append(product + "\n\n\n");
        }*/
    }
}
