package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.adapters.CategoryImagesViewPagerAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.FeaturedVideoAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.RoomCategoriesAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.TestimonialsAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HotelRoomPage extends AppCompatActivity {

//    CollapsingToolbarLayout collapsingToolbar;
//    Toolbar toolbar;

    WebView aboutWebview;


    RecyclerView roomCategoryList, banquetCategoryList, testimonialsList, videosList;

    LinearLayout phoneLay, emailLay, addressLay, aboutLay, contactLay;
    LinearLayout contactViewcontainer;
    TextView phoneTV, emailTV, addressTV, banquetHeader, videoHeader;
    CardView testimonialsLay;

    TextView viewAllBtn;

    RoomCategoriesAdapter roomAdapter, banquetAdapter;
    TestimonialsAdapter testimonialsAdapter;
    FeaturedVideoAdapter videoAdapter;

    ArrayList<String> roomCatIdList = new ArrayList<>();
    ArrayList<String> roomCatNameList = new ArrayList<>();
    ArrayList<String> roomCatTerifList = new ArrayList<>();
    ArrayList<String> roomCatImageList = new ArrayList<>();



    ArrayList<String> testimonialIdList = new ArrayList<>();
    ArrayList<String> testimonialNameList = new ArrayList<>();
    ArrayList<String> testimonialRatingList = new ArrayList<>();
    ArrayList<String> testimonialCommentList = new ArrayList<>();

    ArrayList<String> videoUrlList = new ArrayList<>();
    ArrayList<String> videoThumbUrlList = new ArrayList<>();

    String phoneNo, emailId, address;

    CategoryImagesViewPagerAdapter adapter;
    ArrayList<String> imageUrlList = new ArrayList<>();
    AutoScrollViewPager featuredImageVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_page);


//        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);

        roomCategoryList = findViewById(R.id.hotel_room_category_list);
        banquetCategoryList = findViewById(R.id.hotel_banquet_category_list);
        banquetHeader = findViewById(R.id.hotel_banquet_category_header);
        testimonialsList = findViewById(R.id.hotel_testimonials_list);
        videosList = findViewById(R.id.hotel_video_list);
        videoHeader = findViewById(R.id.hotel_video_header);

        aboutLay = findViewById(R.id.hotel_about_lay);
        contactLay = findViewById(R.id.hotel_contact_lay);
        contactViewcontainer = findViewById(R.id.hotel_contact_viewContainer);
        testimonialsLay = findViewById(R.id.hotel_testimonials_lay);

        aboutWebview = findViewById(R.id.hotel_about_webview);
        featuredImageVP = findViewById(R.id.hotel_featuredImage_viewpager);

        viewAllBtn = findViewById(R.id.hotel_viewall_btn);


        phoneLay = findViewById(R.id.hotel_about_phoneLay);
        emailLay = findViewById(R.id.hotel_about_mailLay);
        addressLay = findViewById(R.id.hotel_about_addressLay);

        phoneTV = findViewById(R.id.hotel_about_phoneTV);
        emailTV = findViewById(R.id.hotel_about_mailTV);
        addressTV = findViewById(R.id.hotel_about_addressTV);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        roomAdapter = new RoomCategoriesAdapter(HotelRoomPage.this, roomCatIdList, roomCatNameList, roomCatImageList, roomCatTerifList,"room", getIntent().getStringExtra(Constants.hotelId), getIntent().getStringExtra(Constants.bookingParams));
        roomCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomCategoryList.setItemAnimator(new DefaultItemAnimator());
        roomCategoryList.setAdapter(roomAdapter);


        banquetCategoryList.setVisibility(View.GONE);
        banquetHeader.setVisibility(View.GONE);

        testimonialsAdapter = new TestimonialsAdapter(HotelRoomPage.this, testimonialIdList, testimonialNameList, testimonialRatingList, testimonialCommentList);
        testimonialsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        testimonialsList.setItemAnimator(new DefaultItemAnimator());
        testimonialsList.setAdapter(testimonialsAdapter);

        videoAdapter = new FeaturedVideoAdapter(HotelRoomPage.this, videoUrlList, videoThumbUrlList);
        videosList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videosList.setItemAnimator(new DefaultItemAnimator());
        videosList.setAdapter(videoAdapter);

        adapter = new CategoryImagesViewPagerAdapter(HotelRoomPage.this, imageUrlList);

        //TODO add runtime permission and add action in contact layouts

        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent asd = new Intent(getApplicationContext(), Testimonials.class);
                asd.putExtra("hotelId", getIntent().getStringExtra(Constants.hotelId));
                startActivity(asd);
            }
        });

        aboutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aboutWebview.getVisibility() == View.VISIBLE) {
                    aboutWebview.setVisibility(View.GONE);
                } else {
                    aboutWebview.setVisibility(View.VISIBLE);
                }
            }
        });

        contactLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contactViewcontainer.getVisibility() == View.VISIBLE) {
                    contactViewcontainer.setVisibility(View.GONE);
                } else {
                    contactViewcontainer.setVisibility(View.VISIBLE);
                }
            }
        });

        getHotelDetailsApi();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getHotelDetailsApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Constants.getHotelDetailsUrl;
        Log.e("URL", url);
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

                            JSONObject hotelData = object.getJSONObject("hotel");

//                            collapsingToolbar.setTitle(hotelData.getString("hotel_name").split(",")[0]);
                            aboutWebview.loadData(hotelData.getString("hotel_desc"), "text/html", "utf-8");

                            phoneNo = hotelData.getString("hotel_contact");
                            emailId = hotelData.getString("hotel_email");
                            address = hotelData.getString("hotel_address");

                            phoneTV.setText(phoneNo);
                            emailTV.setText(emailId);
                            addressTV.setText(address);

                            JSONArray roomData = object.getJSONArray("room");
                            for(int i =0; i<roomData.length(); i++) {
                                roomCatIdList.add(roomData.getJSONObject(i).getString("category_id"));
                                roomCatNameList.add(roomData.getJSONObject(i).getString("category_name"));
                                roomCatImageList.add(roomData.getJSONObject(i).getString("category_image"));
                                roomCatTerifList.add(roomData.getJSONObject(i).getString("category_price"));
                            }
                            roomAdapter.notifyDataSetChanged();

                            JSONArray testimonialData = object.getJSONArray("testimonial");
                            if(testimonialData.length() == 0) {
                                testimonialsLay.setVisibility(View.GONE);
                            } else {
                                for(int i =0; i<testimonialData.length(); i++) {
                                    testimonialIdList.add(testimonialData.getJSONObject(i).getString("testimonial_id"));
                                    testimonialNameList.add(testimonialData.getJSONObject(i).getString("user_name"));
                                    testimonialRatingList.add(testimonialData.getJSONObject(i).getString("rating"));
                                    testimonialCommentList.add(testimonialData.getJSONObject(i).getString("comment"));
                                }
                            }
                            testimonialsAdapter.notifyDataSetChanged();

                            JSONArray imagesArray = object.getJSONArray("images");
                            if(imagesArray.length() != 0) {
                                for(int i = 0; i<imagesArray.length(); i++) {
                                    imageUrlList.add(imagesArray.getJSONObject(i).getString("image_path"));
                                }
                                adapter.notifyDataSetChanged();
                                featuredImageVP.setAdapter(adapter.setInfiniteLoop(true));
                                featuredImageVP.setInterval(2000);
                                featuredImageVP.startAutoScroll();
                            }

                            JSONArray videoData = object.getJSONArray("videos");
                            videoAdapter.notifyDataSetChanged();
                            if(videoData.length() == 0) {
                                videoHeader.setVisibility(View.GONE);
                            } else {
                                for(int i =0; i<videoData.length(); i++) {
                                    videoUrlList.add(videoData.getJSONObject(i).getString("video_path"));
                                    videoThumbUrlList.add(videoData.getJSONObject(i).getString("thumb_path"));
                                }
                            }


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
                        Toast.makeText(HotelRoomPage.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("hotelId", getIntent().getStringExtra(Constants.hotelId));
                Log.e("Params", params.toString());
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(HotelRoomPage.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        featuredImageVP.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        featuredImageVP.startAutoScroll();
    }

    public void setUpBookingParams() {
        String hotelId;
    }

}
