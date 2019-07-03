package com.pelisoftsolutions.hotelsamdariya;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.adapters.GalleryAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BanuquetDetail extends AppCompatActivity {

    String source = "";

    WebView aboutView;
    Toolbar toolbar;
    ImageView featuredIV;
    Button bookBtn;
    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView galleryImageList;
    GalleryAdapter adapter;

    private String selectedCheckInDate = "", selectedCheckOutDate = "", selectedPersonQty = "", selectedRoomQty = "";

    private String catName;

    private Map<String, String> banquetBookingParams = new HashMap<>();

    ArrayList<String> imagePathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banquet_details);


        source = getIntent().getStringExtra("source");

        aboutView = findViewById(R.id.banquet_about_webview);
        featuredIV = findViewById(R.id.banquet_featuredImageIV);
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        bookBtn = findViewById(R.id.banquet_bookBtn);
        galleryImageList  = findViewById(R.id.banquet_gallery_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getCategoryDetailsApi();

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.loginStatus)) {
                    openBottomSheet();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BanuquetDetail.this);
                    builder.setMessage(R.string.loginMessage);
                    builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    });
                }
            }
        });

        adapter = new GalleryAdapter(BanuquetDetail.this, imagePathList);
        galleryImageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryImageList.setItemAnimator(new DefaultItemAnimator());
        galleryImageList.setAdapter(adapter);

    }

    private void getCategoryDetailsApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getCategoryDetailUrl;
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

                            JSONObject catDetails = object.getJSONObject("catDetail");

                            catName = catDetails.getString("name");

                            aboutView.loadData(catDetails.getString("about"), "text/html", "utf-8");
                            collapsingToolbar.setTitle(catName);

                            Picasso.with(getApplicationContext()).load(catDetails.getString("featuredImage")).fit().centerCrop().into(featuredIV);

                            JSONArray imagesArray = object.getJSONArray("images");
                            for(int i = 0; i<imagesArray.length(); i++) {
                                imagePathList.add(imagesArray.getJSONObject(i).getString("image_path"));
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
                        Toast.makeText(BanuquetDetail.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("catId", getIntent().getStringExtra("id"));
                params.put("catType", getIntent().getStringExtra("source"));

                Log.e("Params", params.toString());
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(BanuquetDetail.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("STAUS", "back pressed");
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openBottomSheet() {


        View view = getLayoutInflater().inflate(R.layout.bottomsheet_banquet_book, null);
        view.setMinimumHeight(500);

        TextView header = view.findViewById(R.id.fees_bottomSheet_header);
        ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);

        RelativeLayout checkInDateLay = view.findViewById(R.id.dashboard_checkIn_lay);
        RelativeLayout checkOutDateLay = view.findViewById(R.id.dashboard_checkOut_lay);
        final TextView checkInDateTV = view.findViewById(R.id.dashboard_checkInDate_label);
        final TextView  checkOutDateTV = view.findViewById(R.id.dashboard_checkOutDate_label);

        final EditText eventTypeET = view.findViewById(R.id.dashboard_eventType_et);

        Button submitBtn = view.findViewById(R.id.dashboard_searchBox_submit);

        final BottomSheetDialog dialog = new BottomSheetDialog(BanuquetDetail.this);

        dialog.setContentView(view);
        dialog.show();

        header.setText(catName);

        checkInDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int startYear = c.get(Calendar.YEAR);
                int starthMonth = c.get(Calendar.MONTH);
                int startDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(BanuquetDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedCheckInDate = year+"-"+(++month)+"-"+dayOfMonth;
                        checkInDateTV.setText(selectedCheckInDate);
                    }
                }, startYear, starthMonth, startDay);

                datePickerDialog.show();

            }
        });

        checkOutDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int startYear = c.get(Calendar.YEAR);
                int starthMonth = c.get(Calendar.MONTH);
                int startDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(BanuquetDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedCheckOutDate = year+"-"+(++month)+"-"+dayOfMonth;
                        checkOutDateTV.setText(selectedCheckOutDate);
                    }
                }, startYear, starthMonth, startDay);

                datePickerDialog.show();

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedCheckInDate.isEmpty()) {
                    Toast.makeText(BanuquetDetail.this.getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                } else if (selectedCheckOutDate.isEmpty()) {
                    Toast.makeText(BanuquetDetail.this.getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                } else if (eventTypeET.getText().toString().isEmpty()) {
                    Toast.makeText(BanuquetDetail.this.getApplicationContext(), "Please Enter Type Of Event", Toast.LENGTH_LONG).show();
                } else {
                    banquetBookingParams.put("userId", Utility.getSharedPreferences(BanuquetDetail.this.getApplicationContext(), Constants.userId));
                    banquetBookingParams.put("hotelId", getIntent().getStringExtra("hotelId"));
                    banquetBookingParams.put("catId", getIntent().getStringExtra("id"));
                    banquetBookingParams.put("catType", "banquet");
                    banquetBookingParams.put("startDate", selectedCheckInDate);
                    banquetBookingParams.put("endDate", selectedCheckOutDate);
                    banquetBookingParams.put("eventType", eventTypeET.getText().toString());

                    Log.e("Banquet Booking Params", banquetBookingParams.toString());

                    bookingApi();

                }

            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void bookingApi () {

        final ProgressDialog pd = new ProgressDialog(BanuquetDetail.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.bookingUrl;
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

                            final Dialog dialog = new Dialog(BanuquetDetail.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.success_alert);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);

                            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BalooBhai-Regular.ttf");

                            successHead.setTypeface(custom_font);
                            successMessage.setTypeface(custom_font);

                            successMessage.setText(object.getString("message"));

                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                            dialog.show();

                        } else {

                            final Dialog dialog = new Dialog(BanuquetDetail.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.success_alert);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            ImageView successImage = (ImageView) dialog.findViewById(R.id.successAlert_image);
                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);
                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);

                            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BalooBhai-Regular.ttf");

                            successHead.setTypeface(custom_font);
                            successMessage.setTypeface(custom_font);

                            successHead.setText("Error!");
                            successMessage.setText(object.getString("message"));
                            successImage.setImageDrawable(getResources().getDrawable(R.drawable.img_fail));
                            dialogButton.setText("Try Again");


                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

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
                        Toast.makeText(BanuquetDetail.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return banquetBookingParams;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(BanuquetDetail.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
