package com.coders.goodest.scannbuy.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.coders.goodest.scannbuy.MainActivity;
import com.coders.goodest.scannbuy.R;
import com.coders.goodest.scannbuy.utils.PasswordUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.entity.StringEntity;


/**
 * Created by Mateusz Rajch on 02.05.2018.
 */
public class UserService {
    private static final String API_URL = "http://scanandbuy.000webhostapp.com/api/";

    public void login(final Context context, final String email, String password) throws Exception {
        String loginUrl = API_URL + "login.php";

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("LOGIN", email);
        jsonParams.put("HASLO", PasswordUtils.SHA1(password));
        StringEntity entity = new StringEntity(jsonParams.toString());

        client.post(context, loginUrl, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                try {
                    if (response.getString("success").equals("true")) {
                        SharedPreferences sharedPreferences
                                = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("userEmail", email).apply();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, R.string.badCredentials, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONArray error) {
                Log.d("login error ", statusCode + " " + throwable.getMessage());
            }

        });
    }

}
