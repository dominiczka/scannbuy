package com.coders.goodest.scannbuy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coders.goodest.scannbuy.CartActivity;
import com.coders.goodest.scannbuy.R;

public class ScanFragment extends Fragment implements CartActivity.OnUpdateViewListener {
    //private ScanFragmentActivityListener listener;
    TextView productName;
    TextView productPrice;
    TextView productDescription;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);


        Log.d("FRAGMENT", "wejscie");
        productName = view.findViewById(R.id.product);
        productPrice = view.findViewById(R.id.price);
        productDescription=view.findViewById(R.id.product_description);
        return view;
    }


    @Override
    public void onUpdateView(Bundle bundle) {
        if (bundle!=null){
            String prodName =  bundle.getString("nazwa");
            Log.i("nazwa we fragmecie: ", prodName + " ");
            Float prodPrice= bundle.getFloat("cena");
            Log.i("cena we fragmecie: " , prodPrice + "");
            String prodDesc =  bundle.getString("opis");
            productName.setText(prodName);
            productPrice.setText(prodPrice + " ");
            productDescription.setText(prodDesc);
        }
    }

}
