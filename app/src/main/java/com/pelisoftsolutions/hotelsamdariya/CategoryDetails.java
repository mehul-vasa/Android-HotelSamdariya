package com.pelisoftsolutions.hotelsamdariya;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CategoryDetails extends AppCompatActivity {

    String source = "";

    WebView aboutView;
    Toolbar toolbar;
    ImageView featuredIV;
    Button bookBtn;
    CollapsingToolbarLayout collapsingToolbar;

    private String selectedCheckInDate = "", selectedCheckOutDate = "", selectedPersonQty = "", selectedRoomQty = "", origen="details", status = "0";
    private String terrif;

    private String catName, roomTerrifAmt;



    private Map<String, String> bookingParams = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);


        source = getIntent().getStringExtra("source");

        aboutView = findViewById(R.id.category_about_webview);
        featuredIV = findViewById(R.id.category_featuredImageIV);
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        bookBtn = findViewById(R.id.category_bookBtn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getCategoryDetailsApi();

        try {
            origen = getIntent().getStringExtra("origen");
            if(origen.isEmpty()) {
                origen = "details";
            } else {
                status = getIntent().getStringExtra("status");
            }
        } catch (NullPointerException e) {
            origen = "details";
        }


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheet();

            }
        });



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
                            roomTerrifAmt = catDetails.getString("price");
                            collapsingToolbar.setTitle(catName);
                            Picasso.with(getApplicationContext()).load(catDetails.getString("featuredImage")).fit().centerCrop().into(featuredIV);

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
                        Toast.makeText(CategoryDetails.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CategoryDetails.this);

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

        if(source.equals("room")) {

            View view = getLayoutInflater().inflate(R.layout.bottomsheet_room_book, null);
            view.setMinimumHeight(1500);

            TextView header = view.findViewById(R.id.fees_bottomSheet_header);
            ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);

            final LinearLayout dateLay = view.findViewById(R.id.room_bottonsheet_dateLay);
            final LinearLayout qtyLay = view.findViewById(R.id.room_bottonsheet_qtyLay);
            final LinearLayout priceLay = view.findViewById(R.id.room_bottonsheet_priceLay);

            priceLay.setVisibility(View.GONE);
            dateLay.setVisibility(View.VISIBLE);
            qtyLay.setVisibility(View.VISIBLE);

            final EditText nameET = view.findViewById(R.id.dashboard_name_et);
            final EditText contactET = view.findViewById(R.id.dashboard_contact_et);
            final RelativeLayout checkInDateLay = view.findViewById(R.id.dashboard_checkIn_lay);
            final RelativeLayout checkOutDateLay = view.findViewById(R.id.dashboard_checkOut_lay);
            final TextView checkInDateTV = view.findViewById(R.id.dashboard_checkInDate_label);
            final TextView  checkOutDateTV = view.findViewById(R.id.dashboard_checkOutDate_label);

            final TextView  roomAmtTV = view.findViewById(R.id.dashboard_roomAmtTv);
            final TextView  taxLabelTV = view.findViewById(R.id.dashboard_taxLabelTv);
            final TextView  taxAmtTv = view.findViewById(R.id.dashboard_taxAmtTv);
            final TextView  totalAmtTv = view.findViewById(R.id.dashboard_totalAmtTv);


            final ScrollableNumberPicker personPicker = view.findViewById(R.id.dashboard_bottomSheet_personPicker);
            final ScrollableNumberPicker roomPicker = view.findViewById(R.id.dashboard_bottomSheet_roomPicker);

            final Button submitBtn = view.findViewById(R.id.dashboard_searchBox_submit);

            final BottomSheetDialog dialog = new BottomSheetDialog(CategoryDetails.this);

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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryDetails.this, new DatePickerDialog.OnDateSetListener() {
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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryDetails.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            selectedCheckOutDate = year+"-"+(++month)+"-"+dayOfMonth;
                            checkOutDateTV.setText(selectedCheckOutDate);
                        }
                    }, startYear, starthMonth, startDay);

                    datePickerDialog.show();

                }
            });

            personPicker.setListener(new ScrollableNumberPickerListener() {
                @Override
                public void onNumberPicked(int value) {

                    selectedPersonQty = value+"";
                    roomPicker.setMinValue(1);
                    priceLay.setVisibility(View.VISIBLE);
                    submitBtn.setText(Utility.calculateTeriff(Integer.parseInt(roomTerrifAmt),
                            Integer.parseInt(selectedRoomQty), selectedCheckInDate, selectedCheckOutDate, roomAmtTV, taxLabelTV, taxAmtTv, totalAmtTv) );
                }
            });

            roomPicker.setListener(new ScrollableNumberPickerListener() {
                @Override
                public void onNumberPicked(int value) {

                    selectedRoomQty = value+"";
                    priceLay.setVisibility(View.VISIBLE);
                    submitBtn.setText(Utility.calculateTeriff(Integer.parseInt(roomTerrifAmt),
                            Integer.parseInt(selectedRoomQty), selectedCheckInDate, selectedCheckOutDate, roomAmtTV, taxLabelTV, taxAmtTv, totalAmtTv) );
                }
            });


            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name = nameET.getText().toString();
                    String contact = contactET.getText().toString();

                    if(name.isEmpty()) {
                        nameET.setError("Please enter guest name");
                    } else if (contact.isEmpty()) {
                        contactET.setError("Please enter guest contact number");
                    } else if (contact.length() != 10) {
                        contactET.setError("Please enter a valid guest contact number");
                    } else if (selectedCheckInDate.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                    } else if (selectedCheckOutDate.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                    } else if (selectedPersonQty.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Number Of Person", Toast.LENGTH_LONG).show();
                    } else if (selectedRoomQty.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Number Of Room", Toast.LENGTH_LONG).show();
                    } else {
                        //TODO booking process
                        bookingParams.put("userId", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                        bookingParams.put("hotelId", getIntent().getStringExtra("hotelId"));
                        bookingParams.put("catId", getIntent().getStringExtra("id"));
                        bookingParams.put("catType", "room");
                        bookingParams.put("startDate", selectedCheckInDate);
                        bookingParams.put("endDate", selectedCheckOutDate);
                        bookingParams.put("pax", selectedPersonQty);
                        bookingParams.put("qty", selectedRoomQty);
                        bookingParams.put("eventType", "");
                        bookingParams.put("gustName", name);
                        bookingParams.put("gustContact", contact);

                        Log.e("Booking Params", bookingParams.toString());

                        //TODO booking api update
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

        } else {

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

            final BottomSheetDialog dialog = new BottomSheetDialog(CategoryDetails.this);

            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(view);
            behavior.setState(BottomSheetBehavior.STATE_SETTLING);


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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryDetails.this, new DatePickerDialog.OnDateSetListener() {
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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryDetails.this, new DatePickerDialog.OnDateSetListener() {
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


                    if(Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.loginStatus)) {

                        if (selectedCheckInDate.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                        } else if (selectedCheckOutDate.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                        } else if (eventTypeET.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Enter Type Of Event", Toast.LENGTH_LONG).show();
                        } else {
                            //TODO booking process
                            bookingParams.put("userId", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                            bookingParams.put("hotelId", getIntent().getStringExtra("hotelId"));
                            bookingParams.put("catId", getIntent().getStringExtra("id"));
                            bookingParams.put("catType", "banquet");
                            bookingParams.put("startDate", selectedCheckInDate);
                            bookingParams.put("endDate", selectedCheckOutDate);
                            bookingParams.put("eventType", eventTypeET.getText().toString());

                            Log.e("Banquet Booking Params", bookingParams.toString());

                            bookingApi();

                        }


                    } else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
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



    }


    private void bookingApi () {

        final ProgressDialog pd = new ProgressDialog(CategoryDetails.this);
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

                            final Dialog dialog = new Dialog(CategoryDetails.this);
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

                            final Dialog dialog = new Dialog(CategoryDetails.this);
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
                        Toast.makeText(CategoryDetails.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return bookingParams;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(CategoryDetails.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
