package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.adapters.MyListAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends BaseActivity {

    String source = "";

    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> hotelIdList = new ArrayList<>();

    MyListAdapter adapter;

    RecyclerView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.listview, null, false);
        contentFrame.addView(contentView, 0);

        listview = findViewById(R.id.listview_list);

        source = getIntent().getStringExtra("source");

        Log.e("SOURCE", source);

        switch (source) {
            case "hotel":

                getHotelListApi();

                adapter = new MyListAdapter(ListActivity.this, idList, nameList, imageList, hotelIdList, source);
                listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                listview.setItemAnimator(new DefaultItemAnimator());
                listview.setAdapter(adapter);

            break;

            case "banquet" :

                getBanquetListApi();

                adapter = new MyListAdapter(ListActivity.this, idList, nameList, imageList, hotelIdList, source);
                listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                listview.setItemAnimator(new DefaultItemAnimator());
                listview.setAdapter(adapter);
            break;
        }

    }

    private void getBanquetListApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getBanquetsUrl;
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

                            JSONArray hotelData = object.getJSONArray("banquets");

                            for (int i = 0; i < hotelData.length(); i++) {
                                idList.add(hotelData.getJSONObject(i).getString("banquets_id"));
                                nameList.add(hotelData.getJSONObject(i).getString("banquet_name") +", "+ hotelData.getJSONObject(i).getString("hotel_name"));
                                imageList.add(hotelData.getJSONObject(i).getString("featured_image"));
                                hotelIdList.add(hotelData.getJSONObject(i).getString("hotel_id"));
                            }
                            adapter.notifyDataSetChanged();

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
                        Toast.makeText(ListActivity.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ListActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getHotelListApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getHotelListUrl;
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

                            JSONArray hotelData = object.getJSONArray("hotels");

                            for (int i = 0; i < hotelData.length(); i++) {
                                idList.add(hotelData.getJSONObject(i).getString("hotel_id"));
                                hotelIdList.add(hotelData.getJSONObject(i).getString("hotel_id"));
                                nameList.add(hotelData.getJSONObject(i).getString("hotel_name"));
                                imageList.add(hotelData.getJSONObject(i).getString("featured_image"));
                            }
                            adapter.notifyDataSetChanged();

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
                        Toast.makeText(ListActivity.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ListActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
