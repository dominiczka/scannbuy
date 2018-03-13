package com.coders.goodest.scannbuy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

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
        float toPay=0;
        for(Product object : productsInCart)
        {
            toPay+= object.getCena()*object.getIlosc_w_koszyku();
        }
        TextView mSummaryTextView = findViewById(R.id.cartSummary);
        mSummaryTextView.setText("Total: "+Float.toString(toPay)+"zł");
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_cart_row_with_image, productsInCart);
        mCartProductsListView.setAdapter(productAdapter);
    }
}
