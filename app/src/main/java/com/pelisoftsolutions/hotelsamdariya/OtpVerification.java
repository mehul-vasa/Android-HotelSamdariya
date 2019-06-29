package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class OtpVerification extends AppCompatActivity implements View.OnKeyListener {

    EditText otpET1, otpET2, otpET3, otpET4;
    String userId = "";
    public Map<String, String> params = new Hashtable<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        final Button buttonConfirm = (Button) findViewById(R.id.otp_submitBtn);
        otpET1 = (EditText) findViewById(R.id.editTextone);
        otpET2 = (EditText) findViewById(R.id.editTexttwo);
        otpET3 = (EditText) findViewById(R.id.editTextthree);
        otpET4 = (EditText) findViewById(R.id.editTextfour);

        userId = getIntent().getStringExtra(Constants.userId);

        final TextView resendBtn = findViewById(R.id.otp_resendOTP);

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.clear();
                params.put("userId", userId);
                resendOtpApi();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otpET1.getText().toString()+otpET2.getText().toString()+otpET3.getText().toString()+otpET4.getText().toString();
                if(otp.isEmpty()) {
                    otpET4.setError("Please Enter Your Otp");
                } else {
                    params.clear();
                    params.put("otp", otp);
                    params.put("userId", userId);

                    verifyOtp();
                }
            }
        });

        otpET1.setOnKeyListener(this);
        otpET2.setOnKeyListener(this);
        otpET3.setOnKeyListener(this);
        otpET4.setOnKeyListener(this);

        otpET1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                otpET2.requestFocus();
            }
        });

        otpET2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpET2.getText().toString().length() == 1) {
                    otpET3.requestFocus();
                } if (otpET2.getText().toString().length() == 0) {
                    otpET2.requestFocus();
                }
            }
        });


        otpET3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpET3.getText().toString().length() == 1) {
                    otpET4.requestFocus();
                } if (otpET3.getText().toString().length() == 0) {
                    otpET2.requestFocus();
                }
            }
        });

        otpET4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utility.hideKeyboard(OtpVerification.this, otpET4);
            }
        });


    }

    private void resendOtpApi() {

        final ProgressDialog pd = new ProgressDialog(OtpVerification.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.resendOtpUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("result", result);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("success");
                        if (success.equals("1")) {

                            String message = object.getString("msg");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


                        } else {
                            if (success.equals("0")) {
                                String errorMsg = object.getString("msg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volly Error :" , volleyError.toString());
                        Toast.makeText(getApplicationContext(), R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Adding parameters
                Log.e("resend params", params.toString());
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerification.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

    }

    private void verifyOtp() {

        final ProgressDialog pd = new ProgressDialog(OtpVerification.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.verifyOtpUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("result", result);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("success");
                        if (success.equals("1")) {

                            JSONObject userData = object.getJSONObject("user");

                            Utility.setSharedPreference(getApplicationContext(), Constants.userId, userData.getString("user_id"));
                            Utility.setSharedPreference(getApplicationContext(), Constants.userName, userData.getString("user_name"));
                            Utility.setSharedPreference(getApplicationContext(), Constants.userContact, userData.getString("contact_number"));
                            Utility.setSharedPreference(getApplicationContext(), Constants.userEmail, userData.getString("email"));
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.loginStatus, true);
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();

                        } else {
                            if (success.equals("0")) {
                                String errorMsg = object.getString("msg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),  R.string.noInternetMsg, Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volly Error :" , volleyError.toString());
                        Toast.makeText(getApplicationContext(),  R.string.slowInternetMsg, Toast.LENGTH_LONG).show();

                        Log.e("Volley Error", volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Adding parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerification.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        if(keyCode == KeyEvent.KEYCODE_DEL) {
            switch (v.getId()) {
                case R.id.editTextone :
                    if(otpET1.getText().toString().length() == 0) {

                    }
                    break;

                case R.id.editTexttwo :
                    if(otpET2.getText().toString().length() == 0) {
                        otpET1.requestFocus();
                    }


                    break;

                case R.id.editTextthree :
                    if(otpET2.getText().toString().length() == 0) {
                        otpET2.requestFocus();
                    }


                    break;

                case R.id.editTextfour :
                    if(otpET3.getText().toString().length() == 0) {
                        otpET3.requestFocus();
                    }
                    break;
            }
        }




        return false;
    }

}
