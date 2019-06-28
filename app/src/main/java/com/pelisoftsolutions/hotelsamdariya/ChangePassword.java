package com.pelisoftsolutions.hotelsamdariya;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {

    EditText oldPassET, newPassET, reNewPassET;
    Button submitBtn;

    Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassET = findViewById(R.id.changePassword_oldPasswordET);
        newPassET = findViewById(R.id.changePassword_newPasswordET);
        reNewPassET = findViewById(R.id.changePassword_re_new_PasswordET);

        submitBtn = findViewById(R.id.changePassword_submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPass = newPassET.getText().toString();
                String oldPass = oldPassET.getText().toString();
                String reNewPass = reNewPassET.getText().toString();

                if(oldPass.isEmpty()) {
                    oldPassET.setError("Please Enter Your Old Password");
                } else if (newPass.isEmpty()) {
                    newPassET.setError("Please Enter Your New Password");
                } else if (reNewPass.isEmpty()) {
                    reNewPassET.setError("Please Re - Enter Your New Password");
                } else if (!newPass.equals(reNewPass)) {
                    reNewPassET.setError("Re-Entered Password Does Not Match");
                } else {
                    params.put("oldPass", oldPass);
                    params.put("newPass", newPass);
                    params.put("userId",  Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                    changePasswordApi();
                }

            }
        });




    }

    private void changePasswordApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.changePasswordUrl;
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


                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);

                            builder.setMessage(object.getString("message"))
                                    .setTitle("Success")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("AlertDialogExample");
                            alert.show();



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
                        Toast.makeText(ChangePassword.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){
                Log.e("login params", params.toString());
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
