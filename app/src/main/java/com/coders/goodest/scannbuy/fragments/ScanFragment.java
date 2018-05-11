package com.coders.goodest.scannbuy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.goodest.scannbuy.CartActivity;
import com.coders.goodest.scannbuy.R;
import com.coders.goodest.scannbuy.ScanActivity;
import com.squareup.picasso.Picasso;

public class ScanFragment extends Fragment implements CartActivity.OnUpdateViewListener {
    public static final String IMAGE_URL = "http://scanandbuy.000webhostapp.com/products/photos/";

    Button buttonPlus;
    Button buttonMinus;
    Button buttonDelete;
    TextView productQuantityCounterTextView;
    TextView productQuantityTextView;

    TextView productNameTextView;
    TextView productPriceTextView;
    TextView productDescriptionTextView;
    ImageView productImage;

    int quantityOfProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Log.d("FRAGMENT", "wejscie");
        productNameTextView = view.findViewById(R.id.product);
        productPriceTextView = view.findViewById(R.id.price);
        productDescriptionTextView = view.findViewById(R.id.product_description);
        productImage = view.findViewById(R.id.imageView);

        buttonPlus = view.findViewById(R.id.button_plus_cart);
        buttonMinus = view.findViewById(R.id.button_minus_cart);
        buttonDelete = view.findViewById(R.id.button_delete_cart);
        productQuantityCounterTextView = view.findViewById(R.id.product_quantity_cart);
        productQuantityTextView = view.findViewById(R.id.product_quantity_TextView_cart);

        return view;
    }


    @Override
    public void onUpdateView(Bundle bundle) {

        if (bundle!=null){

            final String productId =  bundle.getString("id");
            Log.i("id we fragmecie" , productId+"");

            String productName = bundle.getString("nazwa");
            Log.i("nazwa we fragmecie", productName+"");

            Float productPrice = bundle.getFloat("cena");
            Log.i("cena we fragmecie" , productPrice + "");

            String productDesc =  bundle.getString("opis");
            Log.i("opis we fragmecie" , productDesc+"");

            final String productQuantity = bundle.getString("iloscKoszyk");
            Log.i("ilosc we fragmecie" , productQuantity+"");

            String imageUrl = IMAGE_URL + bundle.getString("id") + ".png";
            Picasso.with(getContext()).load(imageUrl).into(productImage);

            productNameTextView.setText(productName);
            productPriceTextView.setText(productPrice + "");
            productDescriptionTextView.setText(productDesc);
            productQuantityCounterTextView.setText(productQuantity + "");

            quantityOfProduct = Integer.valueOf(productQuantity);

            final int quantityInStock = bundle.getInt("iloscStan");

            if(bundle.getString("entryPoint").equals("scanProductOnList")){
                buttonPlus.setVisibility(View.INVISIBLE);
                buttonMinus.setVisibility(View.INVISIBLE);
                buttonDelete.setVisibility(View.INVISIBLE);
            }
            else if (bundle.getString("entryPoint").equals("scanNewProduct")){
                buttonPlus.setVisibility(View.VISIBLE);
                buttonMinus.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.INVISIBLE);

                buttonPlus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        quantityOfProduct++;

                        if(quantityOfProduct > quantityInStock){
                            quantityOfProduct--;
                            Toast.makeText(getActivity().getApplicationContext(), new StringBuilder("Na stanie znajduje sie ").append(quantityInStock).append(" produktow."), Toast.LENGTH_SHORT).show();
                        }

                        productQuantityCounterTextView.setText(quantityOfProduct + "");
                        ((ScanActivity)getActivity()).changeProductQuantity(productId, quantityOfProduct);

                    }

                });

                buttonMinus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        quantityOfProduct--;

                        if(quantityOfProduct < 1)
                            quantityOfProduct++;

                        productQuantityCounterTextView.setText(quantityOfProduct + "");
                        ((ScanActivity)getActivity()).changeProductQuantity(productId, quantityOfProduct);

                    }

                });
            }
            else if (bundle.getString("entryPoint").equals("cartList")){
                buttonPlus.setVisibility(View.VISIBLE);
                buttonMinus.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.VISIBLE);

                buttonDelete.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        ((CartActivity)getActivity()).deleteProductFromCart(productId);

                    }

                });

                buttonPlus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        quantityOfProduct++;

                        if(quantityOfProduct > quantityInStock){
                            quantityOfProduct--;
                            Toast.makeText(getActivity().getApplicationContext(), new StringBuilder("Na stanie znajduje sie ").append(quantityInStock).append(" produktow."), Toast.LENGTH_SHORT).show();
                        }

                        productQuantityCounterTextView.setText(quantityOfProduct + "");
                        ((CartActivity)getActivity()).changeProductQuantity(productId, quantityOfProduct);

                    }

                });

                buttonMinus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        quantityOfProduct--;

                        if(quantityOfProduct < 1)
                            quantityOfProduct++;

                        productQuantityCounterTextView.setText(quantityOfProduct + "");
                        ((CartActivity)getActivity()).changeProductQuantity(productId, quantityOfProduct);

                    }

                });
            }

        }
    }

}
