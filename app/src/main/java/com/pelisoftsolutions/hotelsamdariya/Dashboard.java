package com.pelisoftsolutions.hotelsamdariya;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.pelisoftsolutions.hotelsamdariya.adapters.BanquetAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.FeaturedImagesAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.OfferAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends BaseActivity {

    private NestedScrollView scrollView;

    private MaterialSpinner hotelSpinner; //, categorySpinner;
    private RelativeLayout checkInDateLay, checkOutDateLay;
    private TextView checkInDateTV, checkOutDateTV, offerHeader;
    private Button submitBtn;
    private RecyclerView offersList, imagesList, banquetList;
    private ScrollableNumberPicker personPicker, roomPicker;


    OfferAdapter offerAdapter;
    FeaturedImagesAdapter imagesAdapter;
    BanquetAdapter banquetAdapter;
    ArrayAdapter categoryAdapter;

    Map<String, String> params = new HashMap<>();
    Map<String, String> bookingParams = new HashMap<>();

    ArrayList<String> hotelIdList = new ArrayList<>();
    ArrayList<String> hotelNameList = new ArrayList<>();
    ArrayList<String> hotelImageList = new ArrayList<>();

    ArrayList<String> banquetIdList = new ArrayList<>();
    ArrayList<String> banquetNameList = new ArrayList<>();
    ArrayList<String> banquetImageList = new ArrayList<>();
    ArrayList<String> banquetHotelIdList = new ArrayList<>();

    ArrayList<String> catIdList = new ArrayList<>();
    ArrayList<String> catNameList = new ArrayList<>();

    ArrayList<String> offerIdList = new ArrayList<>();
    ArrayList<String> offerTitleList = new ArrayList<>();
    ArrayList<String> offerImageList = new ArrayList<>();

    String selectedHotelId = "", selectedCheckInDate = "", selectedCheckOutDate = "";
    String selectedPersonQty = "", selectedRoomQty = ""; //selectedCatId = "",


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dashboard_activity_content, null, false);
        contentFrame.addView(contentView, 0);


        //HEADER
        hotelSpinner = findViewById(R.id.dashboard_hotels_spinner);
//        categorySpinner = findViewById(R.id.dashboard_bottomsheet_category_spinner);

        checkInDateLay = findViewById(R.id.dashboard_checkIn_lay);
        checkOutDateLay = findViewById(R.id.dashboard_checkOut_lay);
        checkInDateTV = findViewById(R.id.dashboard_checkInDate_label);
        checkOutDateTV = findViewById(R.id.dashboard_checkOutDate_label);

        personPicker = findViewById(R.id.dashboard_bottomSheet_personPicker);
        roomPicker = findViewById(R.id.dashboard_bottomSheet_roomPicker);

        submitBtn = findViewById(R.id.dashboard_searchBox_submit);
        //HEADER

        offerHeader = findViewById(R.id.dashboard_offerHeader);
        offersList = findViewById(R.id.dashboard_offerList);
        imagesList = findViewById(R.id.dashboard_featuredImageList);
        banquetList = findViewById(R.id.dashboard_banquetList);

        offerAdapter = new OfferAdapter(Dashboard.this, offerIdList, offerTitleList, offerImageList);
        offersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        offersList.setItemAnimator(new DefaultItemAnimator());
        offersList.setAdapter(offerAdapter);

        imagesAdapter = new FeaturedImagesAdapter(Dashboard.this, hotelIdList, hotelNameList, hotelImageList);
        imagesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesList.setItemAnimator(new DefaultItemAnimator());
        imagesList.setAdapter(imagesAdapter);

        banquetAdapter = new BanquetAdapter(Dashboard.this, banquetIdList, banquetNameList, banquetImageList, banquetHotelIdList);
        banquetList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        banquetList.setItemAnimator(new DefaultItemAnimator());
        banquetList.setAdapter(banquetAdapter);

        banquetList.setFocusable(false);

        setUpHeader();

        getHotelListApi();
        getOfferListApi();
        getBanquetListApi();



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(selectedHotelId.isEmpty() || selectedHotelId.equals("0") ) {
                    Toast.makeText(getApplicationContext(), "Please Select Hotel First", Toast.LENGTH_LONG).show();
                } else if (selectedCheckInDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                } else if (selectedCheckOutDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                } else if (selectedPersonQty.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Number Of Person", Toast.LENGTH_LONG).show();
                } else if (selectedRoomQty.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Number Of Room", Toast.LENGTH_LONG).show();
                } else {
                    bookingParams.put("userId", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                    bookingParams.put("hotelId", selectedHotelId);
                    bookingParams.put("catId", "0");
                    bookingParams.put("catType", "room");
                    bookingParams.put("startDate", selectedCheckInDate);
                    bookingParams.put("endDate", selectedCheckOutDate);
                    bookingParams.put("pax", selectedPersonQty);
                    bookingParams.put("qty", selectedRoomQty);

                    JSONObject bookingJson;
                    try {
                        bookingJson = new JSONObject(bookingParams.toString());
                    } catch (JSONException w) {
                        bookingJson = new JSONObject();
                        Log.e("parsing error", w.toString());
                    }

                    Log.e("booking json", bookingJson.toString());

                    Intent asd = new Intent(getApplicationContext(), HotelRoomPage.class);
                    asd.putExtra(Constants.hotelId, selectedHotelId);
                    asd.putExtra(Constants.bookingParams, bookingJson.toString());
                    startActivity(asd);

                }


//                if(Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.loginStatus)) {
//
//                    if(selectedHotelId.isEmpty() || selectedHotelId.equals("0") ) {
//                        Toast.makeText(getApplicationContext(), "Please Select Hotel First", Toast.LENGTH_LONG).show();
//                    } else if (selectedCheckInDate.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
//                    } else if (selectedCheckOutDate.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
//                    } else if (selectedPersonQty.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Please Select Number Of Person", Toast.LENGTH_LONG).show();
//                    } else if (selectedRoomQty.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Please Select Number Of Room", Toast.LENGTH_LONG).show();
//                    } else {
//                        bookingParams.put("userId", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
//                        bookingParams.put("hotelId", selectedHotelId);
//                        bookingParams.put("catId", "0");
//                        bookingParams.put("catType", "room");
//                        bookingParams.put("startDate", selectedCheckInDate);
//                        bookingParams.put("endDate", selectedCheckOutDate);
//                        bookingParams.put("pax", selectedPersonQty);
//                        bookingParams.put("qty", selectedRoomQty);
//
//                        JSONObject bookingJson;
//                        try {
//                            bookingJson = new JSONObject(bookingParams.toString());
//                        } catch (JSONException w) {
//                            bookingJson = new JSONObject();
//                            Log.e("parsing error", w.toString());
//                        }
//
//                        Log.e("booking json", bookingJson.toString());
//
//                        Intent asd = new Intent(getApplicationContext(), HotelRoomPage.class);
//                        asd.putExtra(Constants.hotelId, selectedHotelId);
//                        asd.putExtra(Constants.bookingParams, bookingJson.toString());
//                        startActivity(asd);
//
//                    }
//
//                } else {
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                    finish();
//                }

            }
        });



    }

    private void setUpHeader() {


        hotelSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedHotelId = hotelIdList.get(position);
//                getCategoryListApi(hotelIdList.get(position));
            }
        });

//        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                selectedCatId = catIdList.get(position);
//            }
//        });

        checkInDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int startYear = c.get(Calendar.YEAR);
                int starthMonth = c.get(Calendar.MONTH);
                int startDay = c.get(Calendar.DAY_OF_MONTH);

                if(!selectedCheckInDate.isEmpty()) {
                    startYear = Integer.parseInt(selectedCheckInDate.split("-")[0]);
                    starthMonth = Integer.parseInt(selectedCheckInDate.split("-")[1]) - 1;
                    startDay = Integer.parseInt(selectedCheckInDate.split("-")[2]);
                }

                Log.e("startDay", startDay+"..");

                final DatePickerDialog datePickerDialog = new DatePickerDialog(Dashboard.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedCheckInDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        checkInDateTV.setText(dayOfMonth + "-" + (month+1) + "-" + year );
                        Log.e("selectedCheckInDate", selectedCheckInDate);
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

                if(!selectedCheckOutDate.isEmpty()) {
                    startYear = Integer.parseInt(selectedCheckOutDate.split("-")[0]);
                    starthMonth = Integer.parseInt(selectedCheckOutDate.split("-")[1]) - 1;
                    startDay = Integer.parseInt(selectedCheckOutDate.split("-")[2]);
                }


                final DatePickerDialog datePickerDialog = new DatePickerDialog(Dashboard.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String checkOut = year+"-"+(month + 1)+"-"+dayOfMonth;

                        Date checkInDate = Utility.convertStringToDate(selectedCheckInDate, "yyyy-MM-dd");
                        Date checkOutDate = Utility.convertStringToDate(checkOut, "yyyy-MM-dd");

                        if(checkInDate.after(checkOutDate)) {

                            Toast.makeText(getApplicationContext(), "Check Out date cannot be before Check In Date", Toast.LENGTH_LONG).show();
                        } else {

                            selectedCheckOutDate = checkOut;
                            checkOutDateTV.setText(dayOfMonth + "-" + (month+1) + "-" + year );
                        }

                    }
                }, startYear, starthMonth, startDay);


                if(selectedCheckInDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select check-in date first", Toast.LENGTH_LONG).show();
                } else {
                    datePickerDialog.show();
                }



            }
        });

        personPicker.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {

                selectedPersonQty = value+"";
                roomPicker.setMinValue(1);
                roomPicker.setMaxValue(value);
            }
        });

        roomPicker.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {


                selectedRoomQty = value+"";

            }
        });



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

                            hotelIdList.add("0");
                            hotelNameList.add("Select Hotel");

                            for (int i = 0; i < hotelData.length(); i++) {
                                hotelIdList.add(hotelData.getJSONObject(i).getString("hotel_id"));
                                hotelNameList.add(hotelData.getJSONObject(i).getString("hotel_name"));
                                hotelImageList.add(hotelData.getJSONObject(i).getString("featured_image"));
                            }

                            ArrayAdapter hotelListAdapter = new ArrayAdapter(Dashboard.this, android.R.layout.simple_spinner_item, hotelNameList.toArray());
                            hotelListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            hotelSpinner.setAdapter(hotelListAdapter);
                            selectedHotelId = hotelIdList.get(0);

//                            getCategoryListApi(hotelIdList.get(0));

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
                        Toast.makeText(Dashboard.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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
                                banquetIdList.add(hotelData.getJSONObject(i).getString("banquets_id"));
                                banquetNameList.add(hotelData.getJSONObject(i).getString("banquet_name") +", "+ hotelData.getJSONObject(i).getString("hotel_name"));
                                banquetImageList.add(hotelData.getJSONObject(i).getString("featured_image"));
                                banquetHotelIdList.add(hotelData.getJSONObject(i).getString("hotel_id"));
                            }
                            banquetAdapter.notifyDataSetChanged();

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
                        Toast.makeText(Dashboard.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getOfferListApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getOfferListUrl;
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

                            JSONArray offerData = object.getJSONArray("offers");

                            if(offerData.length() != 0) {
                                for (int i = 0; i < offerData.length(); i++) {
                                    offerIdList.add(offerData.getJSONObject(i).getString("offer_id"));
                                    offerTitleList.add(offerData.getJSONObject(i).getString("offer_title"));
                                    offerImageList.add(offerData.getJSONObject(i).getString("offer_image"));
                                }
                                offerAdapter.notifyDataSetChanged();
                            } else {
                                offersList.setVisibility(View.GONE);
                                offerHeader.setVisibility(View.GONE);
                            }
                        } else {
//                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            offersList.setVisibility(View.GONE);
                            offerHeader.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
//                        Toast.makeText(Dashboard.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
