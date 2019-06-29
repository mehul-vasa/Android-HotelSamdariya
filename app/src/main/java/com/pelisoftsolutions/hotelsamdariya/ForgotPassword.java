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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText loginIdTV;
    Button submitBtn;
    TextView loginTV, signUpTV;

    Map<String, String> params = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        loginIdTV = findViewById(R.id.fp_loginIdTV);
        submitBtn = findViewById(R.id.fp_submitBtn);
        loginTV = findViewById(R.id.fp_loginTV);
        signUpTV = findViewById(R.id.fp_signUpTV);


        submitBtn.setOnClickListener(this);
        loginTV.setOnClickListener(this);
        signUpTV.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Log.e("TITLE", v.getTag()+"");

        switch (v.getId()) {

            case R.id.fp_submitBtn :
                String loginId = loginIdTV.getText().toString();
                if(loginId.isEmpty()) {
                    loginIdTV.setError("Please Enter Your Registered Email Id");
                } else {
                    params.put("user_id", loginId);
                    forgotPasswordApi();
                }
                break;

            case R.id.fp_loginTV :
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;

            case R.id.fp_signUpTV :
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                break;

        }

    }

    private void forgotPasswordApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.forgotPasswordUrl;
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
                            Intent asd = new Intent(getApplicationContext(), Login.class);
                            startActivity(asd);

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
                        Toast.makeText(ForgotPassword.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
