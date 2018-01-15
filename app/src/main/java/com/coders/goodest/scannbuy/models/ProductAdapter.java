package com.coders.goodest.scannbuy.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coders.goodest.scannbuy.R;

import java.util.ArrayList;

/**
 * Created by Dominiczka on 15.01.2018.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ProductAdapter(Context context, int resource, ArrayList<Product> productList) {
        super(context, resource, productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.product_cart_row, null);
        }

        Product product = getItem(position);

        if (product != null) {
            TextView mProductNameTextView = view.findViewById(R.id.product_name_textView);
            TextView mProductPriceTextView = view.findViewById(R.id.product_price_textView);
            TextView mProductDescriptionTextView = view.findViewById(R.id.product_description_textView);

            mProductNameTextView.setText(product.getNazwa());
            mProductPriceTextView.setText(Float.toString(product.getCena()));
            mProductDescriptionTextView.setText(product.getOpis());
        }
        return view;
    }
}
