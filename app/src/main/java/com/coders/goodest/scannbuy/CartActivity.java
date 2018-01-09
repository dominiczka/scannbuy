package com.coders.goodest.scannbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.coders.goodest.scannbuy.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> cartProductList;
    TextView mCartProductsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartProductsTextView = findViewById(R.id.cartProductTextView);
        Bundle extra = getIntent().getBundleExtra("extra");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cartProductList= null;
            } else {
                cartProductList= (ArrayList<String>) extra.getSerializable("objects");
            }
        }
        else {
            cartProductList = (ArrayList<String>) savedInstanceState.getSerializable("objects");
        }


        //TODO 4 wypisywanie bez powtorek z uwzględnieniem ilości
        for (String product : cartProductList) {
            mCartProductsTextView.append(product + "\n\n\n");

        }
    }
}
