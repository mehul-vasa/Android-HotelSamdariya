package com.pelisoftsolutions.hotelsamdariya;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.pelisoftsolutions.hotelsamdariya.adapters.BanquetAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.FeaturedImagesAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.OfferAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.DrawerArrowDrawable;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.view.Gravity.START;

public class BaseActivity extends Activity {


    public DrawerArrowDrawable drawerArrowDrawable;
    ImageView drawerIndicator;
    public float offset;
    public boolean flipped;
    public DrawerLayout drawer;
    protected CardView actionBar;

    private NavigationView navigationView;
    private RelativeLayout drawerHead;
    private TextView nameTV;
    public FrameLayout contentFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerIndicator = findViewById(R.id.drawer_indicator);
        actionBar = findViewById(R.id.actionBar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        contentFrame = findViewById(R.id.container);

        setUpDrawer();

    }

    private void setUpDrawer() {

        //HEADER
        View headerLayout = navigationView.getHeaderView(0);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);
        //HEADER

        //TODO dynamic label for login and logout btn;

        Menu navMenu = navigationView.getMenu();
        if(!Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.loginStatus)) {
            navMenu.findItem(R.id.nav_logout).setTitle("Login");
        }



        nameTV.setText(Utility.getSharedPreferences(getApplicationContext(), Constants.userName));


        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });

        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
//                    drawer.closeDrawer(drawerRight);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent dashboard = new Intent(BaseActivity.this, Dashboard.class);
                        startActivity(dashboard);
                        finish();
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_hotel:
                        Intent profile = new Intent(BaseActivity.this, ListActivity.class);
                        profile.putExtra(Constants.source, "hotel");
                        startActivity(profile);
                        finish();
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_banquet:
                        Intent fees = new Intent(BaseActivity.this, ListActivity.class);
                        fees.putExtra(Constants.source, "banquet");
                        startActivity(fees);
                        finish();
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_changePassword:
                        Intent classTimeTable = new Intent(BaseActivity.this, ChangePassword.class);
                        classTimeTable.putExtra(Constants.source, "about");
                        startActivity(classTimeTable);
                        finish();
                        drawer.closeDrawer(START);
                        break;


                    case R.id.nav_logout:
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.loginStatus, false);
                        Utility.setSharedPreference(getApplicationContext(), Constants.userId, "null");
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                        drawer.closeDrawer(START);
                        break;


                }


                return false;
            }
        });


    }



//    private void getCategoryListApi (final String hotelId) {
//
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Loading");
//        pd.setCancelable(false);
//        pd.show();
//
//        String url = Constants.getRoomCategoriesUrl;
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                if (result != null) {
//                    pd.dismiss();
//                    try {
//                        Log.e("Result", result);
//                        JSONObject object = new JSONObject(result);
//
//                        String status = object.getString("status");
//                        catNameList.clear();
//                        if(status.equals("1")) {
////                            categorySpinner.setVisibility(View.VISIBLE);
//
//                            JSONArray catData = object.getJSONArray("room_category");
//
//                            for (int i = 0; i < catData.length(); i++) {
//                                catIdList.add(catData.getJSONObject(i).getString("category_id"));
//                                catNameList.add(catData.getJSONObject(i).getString("category_name"));
//                            }
//
////                            categoryAdapter = new ArrayAdapter(BaseActivity.this, android.R.layout.simple_spinner_item, catNameList.toArray());
////                            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                            categorySpinner.setAdapter(categoryAdapter);
//
////                            selectedCatId = catIdList.get(0);
//
//                        } else {
////                            categorySpinner.setVisibility(View.GONE);
//                            catNameList.add("Select Category");
////                            selectedCatId = "0";
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    pd.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        pd.dismiss();
//                        Log.e("Volley Error", volleyError.toString());
//                        Toast.makeText(BaseActivity.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
//                    }
//                }) {
//
//            @Override
//            public Map<String, String> getParams(){
//                params.put("hotelId", hotelId);
//                return params;
//            }
//        };
//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity.this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }

//    private void bookingApi () {
//
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Loading");
//        pd.setCancelable(false);
//        pd.show();
//
//        String url = Constants.bookingUrl;
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                if (result != null) {
//                    pd.dismiss();
//                    try {
//                        Log.e("Result", result);
//                        JSONObject object = new JSONObject(result);
//
//                        String status = object.getString("status");
//
//                        Intent asd = new Intent(getApplicationContext(), CategoryDetails.class);
//                        asd.putExtra("source", "room");
//                        asd.putExtra("hotelId", selectedHotelId);
//                        asd.putExtra("id", "1");
//                        asd.putExtra("origen", "dashboard");
//                        asd.putExtra("status", status);
//                        asd.putExtra("message", object.getString("message"));
//                        startActivity(asd);
//
////                        if(status.equals("1")) {
////
////                            final Dialog dialog = new Dialog(BaseActivity.this);
////                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////                            dialog.setCancelable(true);
////                            dialog.setContentView(R.layout.success_alert);
////                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////
////                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
////                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);
////
////                            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BalooBhai-Regular.ttf");
////
////                            successHead.setTypeface(custom_font);
////                            successMessage.setTypeface(custom_font);
////
////                            successMessage.setText(object.getString("message"));
////
////                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);
////                            dialogButton.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    dialog.dismiss();
////                                }
////                            });
////
////                            dialog.show();
////
////                        } else {
////
////                            final Dialog dialog = new Dialog(BaseActivity.this);
////                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////                            dialog.setCancelable(true);
////                            dialog.setContentView(R.layout.success_alert);
////                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////
////                            ImageView successImage = (ImageView) dialog.findViewById(R.id.successAlert_image);
////                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
////                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);
////                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);
////
////                            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BalooBhai-Regular.ttf");
////
////                            successHead.setTypeface(custom_font);
////                            successMessage.setTypeface(custom_font);
////
////                            successHead.setText("Error!");
////                            successMessage.setText(object.getString("message"));
////                            successImage.setImageDrawable(getResources().getDrawable(R.drawable.img_fail));
////                            dialogButton.setText("Try Again");
////
////
////                            dialogButton.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    dialog.dismiss();
////                                }
////                            });
////
////                            dialog.show();
////
////                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    pd.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        pd.dismiss();
//                        Log.e("Volley Error", volleyError.toString());
//                        Toast.makeText(BaseActivity.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
//                    }
//                }) {
//
//            @Override
//            public Map<String, String> getParams(){
//
//                return bookingParams;
//            }
//        };
//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(BaseActivity.this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }


}
