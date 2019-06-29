package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.pelisoftsolutions.hotelsamdariya.adapters.TestimonialsAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Testimonials extends AppCompatActivity {

    RecyclerView testimonialsList;

    TestimonialsAdapter testimonialsAdapter;

    ArrayList<String> testimonialIdList = new ArrayList<>();
    ArrayList<String> testimonialNameList = new ArrayList<>();
    ArrayList<String> testimonialRatingList = new ArrayList<>();
    ArrayList<String> testimonialCommentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonials);

        testimonialsList = findViewById(R.id.testimonials_listview);

        getTestimonialApi();

        testimonialsAdapter = new TestimonialsAdapter(Testimonials.this, testimonialIdList, testimonialNameList, testimonialRatingList, testimonialCommentList);
        testimonialsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        testimonialsList.setItemAnimator(new DefaultItemAnimator());
        testimonialsList.setAdapter(testimonialsAdapter);


    }

    private void getTestimonialApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getTestimonialsUrl;
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

                            JSONArray testimonialData = object.getJSONArray("testimonials");
                            for(int i =0; i<testimonialData.length(); i++) {
                                testimonialIdList.add(testimonialData.getJSONObject(i).getString("testimonial_id"));
                                testimonialNameList.add(testimonialData.getJSONObject(i).getString("user_name"));
                                testimonialRatingList.add(testimonialData.getJSONObject(i).getString("rating"));
                                testimonialCommentList.add(testimonialData.getJSONObject(i).getString("comment"));
                            }
                            testimonialsAdapter.notifyDataSetChanged();

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
                        Toast.makeText(Testimonials.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("hotelId", getIntent().getStringExtra(Constants.hotelId));

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Testimonials.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
