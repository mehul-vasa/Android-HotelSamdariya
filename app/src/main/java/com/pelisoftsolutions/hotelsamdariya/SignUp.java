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

public class SignUp extends AppCompatActivity {

    EditText nameET, contactET, emailET, passwordET, repassET;
    Button signUpBtn;
    TextView loginBtn, skipBtn;

    Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameET = findViewById(R.id.signup_nameET);
        contactET = findViewById(R.id.signup_contactET);
        emailET = findViewById(R.id.signup_emailET);
        passwordET = findViewById(R.id.signup_passET);
        repassET = findViewById(R.id.signup_rePassET);

        signUpBtn = findViewById(R.id.signup_submitBtn);

        loginBtn = findViewById(R.id.signup_loginBtn);
        skipBtn = findViewById(R.id.signup_guestBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameET.getText().toString().isEmpty()) {
                    nameET.setError("Please enter a valid name");
                } if (emailET.getText().toString().isEmpty()) {
                    emailET.setError("Please enter your email id");
                } if (passwordET.getText().toString().isEmpty()) {
                    passwordET.setError("Please enter your password");
                } if (repassET.getText().toString().isEmpty()) {
                    repassET.setError("Please re-enter your passsword");
                } if (!passwordET.getText().toString().equals(repassET.getText().toString())) {
                    repassET.setError("Password do not match");
                } else {

                    //TODO sign-up api
                    params.put("name", nameET.getText().toString());
                    params.put("contact", contactET.getText().toString());
                    params.put("email", emailET.getText().toString());
                    params.put("password", passwordET.getText().toString());
                    signUpApi();

                }

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("hello", "ASA");
                Utility.setSharedPreference(getApplicationContext(), Constants.userId, "0");
                Utility.setSharedPreference(getApplicationContext(), Constants.userName, "0");
                Utility.setSharedPreference(getApplicationContext(), Constants.userContact, "0");
                Utility.setSharedPreference(getApplicationContext(), Constants.userEmail, "0");
                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.loginStatus, false);
                startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                finish();
            }
        });




    }

    private void signUpApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.signUpUrl;
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

                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();

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
                        Toast.makeText(SignUp.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
