package com.coders.goodest.scannbuy.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coders.goodest.scannbuy.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    public static final String IMAGE_URL = "http://scanandbuy.000webhostapp.com/products/photos/";

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

            view = layoutInflater.inflate(R.layout.product_cart_row_with_image, null);
        }

        Product product = getItem(position);

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        if (product != null) {

            TextView mProductNameTextView = view.findViewById(R.id.productName);
            TextView mProductPriceTextView = view.findViewById(R.id.productPrice);
            TextView mProductDescriptionTextView = view.findViewById(R.id.productDescription);
            TextView mProductQuantityTextView = view.findViewById(R.id.productQuantity);
            TextView mProductOverallPrice = view.findViewById(R.id.productOverallPrice);
            ImageView mProductImage = view.findViewById(R.id.image);

            String imageUrl = IMAGE_URL + product.getId_kod_kreskowy() + ".png";
            Picasso.with(getContext()).load(imageUrl).into(mProductImage);
            mProductNameTextView.setText(product.getNazwa());
            mProductDescriptionTextView.setText(product.getOpis());
            mProductQuantityTextView.setText(Integer.toString(product.getIlosc_w_koszyku()));

            mProductOverallPrice.setText(df.format(round(product.getCena()*product.getIlosc_w_koszyku(),2))+" zł");
            mProductPriceTextView.setText(df.format(product.getCena())+" zł");
        }
        return view;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
