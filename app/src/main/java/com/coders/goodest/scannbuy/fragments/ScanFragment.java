package com.coders.goodest.scannbuy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.coders.goodest.scannbuy.R;
import com.coders.goodest.scannbuy.ScanActivity;

import android.widget.TextView;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;

public class ScanFragment extends Fragment {
    //private ScanFragmentActivityListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ((ScanActivity)getActivity()).showHide();
        OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back:
                        ((ScanActivity)getActivity()).showHide();
                        break;
                    case R.id.cart:

                        break;
                    default:
                        break;
                }
            }
        };
        Button back = (Button) view.findViewById(R.id.back);
        Button cart = (Button) view.findViewById(R.id.cart);

        cart.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);

        return view;
    }


}
