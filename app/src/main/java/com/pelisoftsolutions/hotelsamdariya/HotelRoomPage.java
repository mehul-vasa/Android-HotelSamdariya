package com.pelisoftsolutions.hotelsamdariya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelisoftsolutions.hotelsamdariya.adapters.AminitiesAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.CategoryImagesViewPagerAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.FeaturedVideoAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.RoomCategoriesAdapter;
import com.pelisoftsolutions.hotelsamdariya.adapters.TestimonialsAdapter;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HotelRoomPage extends AppCompatActivity {

    ImageView backBtn;
    TextView actionTitle;

    WebView aboutWebview;

    RecyclerView roomCategoryList, banquetCategoryList, testimonialsList, videosList, aminitiesListview;

    LinearLayout phoneLay, emailLay, addressLay, aboutLay, contactLay;
    LinearLayout contactViewcontainer;
    TextView averageRatingTV, phoneTV, emailTV, addressTV, banquetHeader, videoHeader;
    CardView testimonialsLay, aminitiesLay, policyLay;

    TextView hotelNameTV, hotelLocationTV,  viewAllBtn;

    RoomCategoriesAdapter roomAdapter, banquetAdapter;
    TestimonialsAdapter testimonialsAdapter;
    FeaturedVideoAdapter videoAdapter;
    AminitiesAdapter aminitiesAdapter;
    TestimonialsAdapter bottomSheetAdapter;

    FloatingActionButton whatsappFab;

    ArrayList<String> roomCatIdList = new ArrayList<>();
    ArrayList<String> roomCatNameList = new ArrayList<>();
    ArrayList<String> roomCatDescList = new ArrayList<>();
    ArrayList<String> roomCatTerifList = new ArrayList<>();
    ArrayList<String> roomCatImageList = new ArrayList<>();

    ArrayList<String> aminitiesList = new ArrayList<>();

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

        backBtn = findViewById(R.id.hotel_details_backBtn);
        actionTitle = findViewById(R.id.hotel_details_action_title);

        hotelNameTV = findViewById(R.id.hotel_details_hotelName);
        hotelLocationTV = findViewById(R.id.hotel_details_hotellocationTV);

        roomCategoryList = findViewById(R.id.hotel_room_category_list);
        aminitiesListview = findViewById(R.id.hotel_amenities_list);
        banquetCategoryList = findViewById(R.id.hotel_banquet_category_list);
        banquetHeader = findViewById(R.id.hotel_banquet_category_header);
        testimonialsList = findViewById(R.id.hotel_testimonials_list);
        videosList = findViewById(R.id.hotel_video_list);
        videoHeader = findViewById(R.id.hotel_video_header);

        aboutLay = findViewById(R.id.hotel_about_lay);
        contactLay = findViewById(R.id.hotel_contact_lay);
        contactViewcontainer = findViewById(R.id.hotel_contact_viewContainer);
        testimonialsLay = findViewById(R.id.hotel_testimonials_lay);
        aminitiesLay = findViewById(R.id.hotel_amenities_card);
        policyLay = findViewById(R.id.hotel_policy_card);

        aboutWebview = findViewById(R.id.hotel_about_webview);
        featuredImageVP = findViewById(R.id.hotel_featuredImage_viewpager);

        viewAllBtn = findViewById(R.id.hotel_viewall_btn);

        averageRatingTV = findViewById(R.id.hotel_details_avgRatingTV);
        phoneLay = findViewById(R.id.hotel_about_phoneLay);
        emailLay = findViewById(R.id.hotel_about_mailLay);
        addressLay = findViewById(R.id.hotel_about_addressLay);

        phoneTV = findViewById(R.id.hotel_about_phoneTV);
        emailTV = findViewById(R.id.hotel_about_mailTV);
        addressTV = findViewById(R.id.hotel_about_addressTV);

        whatsappFab = findViewById(R.id.hotels_fab_whatsapp);

        whatsappFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String text = "Hello, I want to book a room in your hotel.";

                    String toNumber = Constants.whatsappNumber;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        roomAdapter = new RoomCategoriesAdapter(HotelRoomPage.this, roomCatIdList, roomCatNameList, roomCatDescList, roomCatImageList, roomCatTerifList,"room", getIntent().getStringExtra(Constants.hotelId), getIntent().getStringExtra(Constants.bookingParams));
        roomCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomCategoryList.setItemAnimator(new DefaultItemAnimator());
        roomCategoryList.setAdapter(roomAdapter);

        aminitiesAdapter = new AminitiesAdapter(HotelRoomPage.this, aminitiesList);
        aminitiesListview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aminitiesListview.setItemAnimator(new DefaultItemAnimator());
        aminitiesListview.setAdapter(aminitiesAdapter);


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
                openListBottomSheet();
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void openListBottomSheet() {

        getTestimonialApi();

        View view = getLayoutInflater().inflate(R.layout.bottomsheet_list, null);
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(view);
//        behavior.setPeekHeight(300);

        TextView header = view.findViewById(R.id.fees_bottomSheet_header);
        ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);


        final RecyclerView listview = view.findViewById(R.id.bottomsheet_listview);

        bottomSheetAdapter = new TestimonialsAdapter(HotelRoomPage.this, testimonialIdList, testimonialNameList, testimonialRatingList, testimonialCommentList);
        listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(bottomSheetAdapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);

        dialog.setContentView(view);
        dialog.show();

        header.setText("REVIEWS");

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

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
                            testimonialIdList.clear(); testimonialNameList.clear(); testimonialRatingList.clear(); testimonialCommentList.clear();
                            JSONArray testimonialData = object.getJSONArray("testimonials");
                            for(int i =0; i<testimonialData.length(); i++) {
                                testimonialIdList.add(testimonialData.getJSONObject(i).getString("testimonial_id"));
                                testimonialNameList.add(testimonialData.getJSONObject(i).getString("user_name"));
                                testimonialRatingList.add(testimonialData.getJSONObject(i).getString("rating"));
                                testimonialCommentList.add(testimonialData.getJSONObject(i).getString("comment"));
                            }
                            bottomSheetAdapter.notifyDataSetChanged();

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
                Log.e("testimonial params", params.toString() );
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(HotelRoomPage.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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

                            hotelNameTV.setText(hotelData.getString("hotel_name").split(",")[0]);
                            actionTitle.setText(hotelData.getString("hotel_name").split(",")[0]);
                            hotelLocationTV.setText(hotelData.getString("hotel_name").split(",")[1]);

                            averageRatingTV.setText(hotelData.getString("avgRating"));

                            aboutWebview.loadData(hotelData.getString("hotel_desc"), "text/html", "utf-8");

                            String aminities = hotelData.getString("hotel_aminities");
                            if(aminities.isEmpty()) {
                                aminitiesLay.setVisibility(View.GONE);
                            } else {
                                List<String> amList = Arrays.asList(aminities.split(","));
                                for (int i = 0; i<amList.size(); i++) {
                                    aminitiesList.add(amList.get(i));
                                }
                                aminitiesAdapter.notifyDataSetChanged();
                            }


                            phoneNo = hotelData.getString("hotel_contact");
                            emailId = hotelData.getString("hotel_email");
                            address = hotelData.getString("hotel_address");

                            phoneTV.setText(phoneNo);
                            emailTV.setText(emailId);
                            addressTV.setText(address);

                            //TODO add privacy here
                            String privacyPolicy = "";
                            if(privacyPolicy.isEmpty()) {
                                policyLay.setVisibility(View.GONE);
                            }

                            JSONArray roomData = object.getJSONArray("room");
                            for(int i =0; i<roomData.length(); i++) {
                                roomCatIdList.add(roomData.getJSONObject(i).getString("category_id"));
                                roomCatNameList.add(roomData.getJSONObject(i).getString("category_name"));
                                roomCatDescList.add(roomData.getJSONObject(i).getString("category_desc"));
                                roomCatImageList.add(roomData.getJSONObject(i).getString("category_image"));
                                roomCatTerifList.add(roomData.getJSONObject(i).getString("category_price"));
                            }
                            roomAdapter.notifyDataSetChanged();
                            Log.e("roomAdapter adapter", roomAdapter.getItemCount()+"..");


                            JSONArray testimonialData = object.getJSONArray("testimonial");
                            Log.e("testimonialData", testimonialData.length()+"..");
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



}
