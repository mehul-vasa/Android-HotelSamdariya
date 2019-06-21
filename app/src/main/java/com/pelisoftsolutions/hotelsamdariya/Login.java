package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button loginBtn;
    TextView forgotPassBtn, signUpBtn, skipBtn;

    EditText loginIdTV, passwordTV;

    Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_loginBtn);
        forgotPassBtn = findViewById(R.id.login_forgotPassword);
        signUpBtn = findViewById(R.id.login_signUp);
        skipBtn = findViewById(R.id.login_skip);

        loginIdTV = findViewById(R.id.login_loginIdTV);
        passwordTV = findViewById(R.id.login_passwordTV);

        if(Constants.isDevelopmentModeOn) {
            loginIdTV.setText("8827271270");
            passwordTV.setText("mehul");
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginId = loginIdTV.getText().toString();
                String password = passwordTV.getText().toString();

                if(loginId.isEmpty()) {
                    loginIdTV.setError("Please Enter Your Login Id");
                } else if (password.isEmpty()) {
                    passwordTV.setError("Please Enter Your Password");
                } else {
                    params.put("user_id", loginId);
                    params.put("password", password);
                    loginApi();
                }


            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.setSharedPreference(getApplicationContext(), Constants.userId, "0");
                Utility.setSharedPreference(getApplicationContext(), Constants.userName, "Guest");
                Utility.setSharedPreference(getApplicationContext(), Constants.userContact, "0");
                Utility.setSharedPreference(getApplicationContext(), Constants.userEmail, "0");
                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.loginStatus, false);
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });


    }

    private void loginApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.loginUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String status = object.getString("status");
                        if(status.equals("1")) {

                            JSONObject userData = object.getJSONObject("userData");

                            if(userData.getString("auth_token").isEmpty()) {
                                Utility.setSharedPreference(getApplicationContext(), Constants.userId, userData.getString("user_id"));
                                Utility.setSharedPreference(getApplicationContext(), Constants.userName, userData.getString("user_name"));
                                Utility.setSharedPreference(getApplicationContext(), Constants.userContact, userData.getString("contact_number"));
                                Utility.setSharedPreference(getApplicationContext(), Constants.userEmail, userData.getString("email"));
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.loginStatus, true);
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                finish();
                            } else {
                                Intent asd = new Intent(getApplicationContext(), OtpVerification.class);
                                asd.putExtra(Constants.userId, userData.getString("user_id"));
                                startActivity(asd);
                                finish();
                            }



                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(Login.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
                public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
