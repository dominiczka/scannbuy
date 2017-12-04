package com.coders.goodest.scannbuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.goodest.scannbuy.barcode.BarcodeCaptureActivity;
import com.cooders.goodest.scannbuy.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;


public class ScanActivity extends AppCompatActivity {

    TextView mShopIdTextView;
    Button mScanButton;
    Button mCartButton;
    private static final int RC_BARCODE_CAPTURE = 9001; //czy to jest potrzebne?

    ArrayList<String> scannedProductList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannedProductList = new ArrayList<String>();
        String shopId; // Czy to na pewno ma być string?
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                shopId= null;
            } else {
                shopId= extras.getString("shopID");
            }
        } else {
            shopId= (String) savedInstanceState.getSerializable("shopID");
        }


        //TODO 1 połączenie z baza, sprawdzenie sklepu
        //TODO 2 zapisanie nazwy sklepu

        mShopIdTextView = findViewById(R.id.shopIdTextView);
        mShopIdTextView.setText(shopId);



        mScanButton = findViewById(R.id.scanProductButton);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });


        mCartButton = findViewById(R.id.cartButton);
        mCartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extra = new Bundle();
                extra.putSerializable("objects", scannedProductList);
                Intent intent = new Intent(ScanActivity.this, CartActivity.class);
                intent.putExtra("extra", extra);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(getApplicationContext(), barcode.rawValue, Toast.LENGTH_SHORT).show();
                    scannedProductList.add(barcode.rawValue);
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
