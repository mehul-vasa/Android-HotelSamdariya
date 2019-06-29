package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.pelisoftsolutions.hotelsamdariya.BanuquetDetail;
import com.pelisoftsolutions.hotelsamdariya.CategoryDetails;
import com.pelisoftsolutions.hotelsamdariya.Login;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RoomCategoriesAdapter extends RecyclerView.Adapter<RoomCategoriesAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> catIdList;
    private ArrayList<String> catNameList;
    private ArrayList<String> catDescList;
    private ArrayList<String> catImageList;
    private ArrayList<String> roomCatTerifList;
    private String source;
    private String hotelId;
    private String bookingData;
    int teriff;

    private Map<String, String> bookingParams = new HashMap<>();

    private String selectedCheckInDate = "", selectedCheckOutDate = "", selectedPersonQty = "", selectedRoomQty = "";

    public RoomCategoriesAdapter(Activity hotelPage, ArrayList<String> roomCatIdList, ArrayList<String> roomCatNameList, ArrayList<String> roomCatDescList, ArrayList<String> roomCatImageList,
                                 ArrayList<String> roomCatTerifList, String source, String hotelId, String bookingData) {

        this.context = hotelPage;
        this.catIdList = roomCatIdList;
        this.catNameList = roomCatNameList;
        this.catDescList = roomCatDescList;
        this.catImageList = roomCatImageList;
        this.roomCatTerifList = roomCatTerifList;
        this.source = source;
        this.hotelId = hotelId;
        this.bookingData = bookingData;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView categoryNameTV, categoryTerifTV, categoryDetailsTV;
        public ImageView categoryImageIV;
        public RelativeLayout viewContainer;
        public Button bookBtn;

        public MyViewHolder(View view) {
            super(view);

            categoryNameTV = (TextView) view.findViewById(R.id.adapter_category_nameTV);
            categoryDetailsTV = (TextView) view.findViewById(R.id.adapter_category_details);
            categoryTerifTV = (TextView) view.findViewById(R.id.adapter_category_terifTV);
            categoryImageIV = (ImageView) view.findViewById(R.id.adapter_category_imageIV);
            viewContainer = (RelativeLayout) view.findViewById(R.id.adapter_category_viewContainer);
            bookBtn = (Button) view.findViewById(R.id.adapter_category_bookBtn);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_room_categories, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.categoryNameTV.setText(catNameList.get(position));
        holder.categoryTerifTV.setText("Rs." + roomCatTerifList.get(position) +"/-");
        holder.categoryDetailsTV.setText(Html.fromHtml(catDescList.get(position)));

        Picasso.with(context).load(catImageList.get(position)).fit().centerCrop().into(holder.categoryImageIV);

        if(!source.equalsIgnoreCase("room")) {
            holder.categoryTerifTV.setVisibility(View.GONE);
        }

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(source.equals("room")) {
                    Intent asd = new Intent(context, CategoryDetails.class);
                    asd.putExtra("source", source);
                    asd.putExtra("hotelId", hotelId);
                    asd.putExtra("id", catIdList.get(position));
                    asd.putExtra(Constants.bookingParams, bookingData);
                    context.startActivity(asd);
                } else {
                    Intent asd = new Intent(context, BanuquetDetail.class);
                    asd.putExtra("source", source);
                    asd.putExtra("hotelId", hotelId);
                    asd.putExtra("id", catIdList.get(position));
                    context.startActivity(asd);
                }

            }
        });

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Source", source);

                JSONObject bookingJson;

                try {
                    bookingJson = new JSONObject(bookingData);
                } catch (JSONException e) {
                    bookingJson = new JSONObject();
                }

                Log.e("booking params sp", bookingData);

                openBottomSheet(position, bookingJson);
            }
        });

    }

    @Override
    public int getItemCount() {
        return catIdList.size();
    }

    private void openBottomSheet(final int position, JSONObject data) {

        if(source.equals("room")) {

            View view = context.getLayoutInflater().inflate(R.layout.bottomsheet_room_book, null);
            view.setMinimumHeight(1500);

            TextView header = view.findViewById(R.id.fees_bottomSheet_header);
            ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);

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

            //TODO load default data
            try {

                selectedCheckInDate = data.getString("startDate");
                selectedCheckOutDate = data.getString("endDate");
                selectedPersonQty = data.getString("pax");
                selectedRoomQty = data.getString("qty");

                submitBtn.setText(Utility.calculateTeriff(Integer.parseInt(roomCatTerifList.get(position)),
                        Integer.parseInt(selectedRoomQty), selectedCheckInDate, selectedCheckOutDate, roomAmtTV, taxLabelTV, taxAmtTv, totalAmtTv) );


                checkInDateTV.setText(selectedCheckInDate);
                checkOutDateTV.setText(selectedCheckOutDate);
                personPicker.setValue(Integer.parseInt(selectedPersonQty));
                roomPicker.setValue(Integer.parseInt(selectedRoomQty));

            } catch (JSONException e) {
                Log.e("Parse exception", e.toString());
            }

            final BottomSheetDialog dialog = new BottomSheetDialog(context);

            dialog.setContentView(view);
            dialog.show();


            header.setText(catNameList.get(position));

            checkInDateLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();
                    int startYear = c.get(Calendar.YEAR);
                    int starthMonth = c.get(Calendar.MONTH);
                    int startDay = c.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                    submitBtn.setText(Utility.calculateTeriff(Integer.parseInt(roomCatTerifList.get(position)),
                            Integer.parseInt(selectedRoomQty), selectedCheckInDate, selectedCheckOutDate, roomAmtTV, taxLabelTV, taxAmtTv, totalAmtTv) );
                }
            });

            roomPicker.setListener(new ScrollableNumberPickerListener() {
                @Override
                public void onNumberPicked(int value) {

                    selectedRoomQty = value+"";
                    submitBtn.setText(Utility.calculateTeriff(Integer.parseInt(roomCatTerifList.get(position)),
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
                        Toast.makeText(context.getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                    } else if (selectedCheckOutDate.isEmpty()) {
                        Toast.makeText(context.getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                    } else if (selectedPersonQty.isEmpty()) {
                        Toast.makeText(context.getApplicationContext(), "Please Select Number Of Person", Toast.LENGTH_LONG).show();
                    } else if (selectedRoomQty.isEmpty()) {
                        Toast.makeText(context.getApplicationContext(), "Please Select Number Of Room", Toast.LENGTH_LONG).show();
                    } else {
                        //TODO booking process
                        bookingParams.put("userId", Utility.getSharedPreferences(context.getApplicationContext(), Constants.userId));
                        bookingParams.put("hotelId", hotelId);
                        bookingParams.put("catId", catIdList.get(position));
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

            View view = context.getLayoutInflater().inflate(R.layout.bottomsheet_banquet_book, null);
            view.setMinimumHeight(500);

            TextView header = view.findViewById(R.id.fees_bottomSheet_header);
            ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);

            RelativeLayout checkInDateLay = view.findViewById(R.id.dashboard_checkIn_lay);
            RelativeLayout checkOutDateLay = view.findViewById(R.id.dashboard_checkOut_lay);
            final TextView checkInDateTV = view.findViewById(R.id.dashboard_checkInDate_label);
            final TextView  checkOutDateTV = view.findViewById(R.id.dashboard_checkOutDate_label);

            final EditText eventTypeET = view.findViewById(R.id.dashboard_eventType_et);

            Button submitBtn = view.findViewById(R.id.dashboard_searchBox_submit);

            final BottomSheetDialog dialog = new BottomSheetDialog(context);

            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(view);
            behavior.setState(BottomSheetBehavior.STATE_SETTLING);


            dialog.setContentView(view);
            dialog.show();

            header.setText(catNameList.get(position));

            checkInDateLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();
                    int startYear = c.get(Calendar.YEAR);
                    int starthMonth = c.get(Calendar.MONTH);
                    int startDay = c.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

                    final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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


                    if(Utility.getSharedPreferencesBoolean(context.getApplicationContext(), Constants.loginStatus)) {

                        if (selectedCheckInDate.isEmpty()) {
                            Toast.makeText(context.getApplicationContext(), "Please Select Check In First", Toast.LENGTH_LONG).show();
                        } else if (selectedCheckOutDate.isEmpty()) {
                            Toast.makeText(context.getApplicationContext(), "Please Select Check Out First", Toast.LENGTH_LONG).show();
                        } else if (eventTypeET.getText().toString().isEmpty()) {
                            Toast.makeText(context.getApplicationContext(), "Please Enter Type Of Event", Toast.LENGTH_LONG).show();
                        } else {
                            //TODO booking process
                            bookingParams.put("userId", Utility.getSharedPreferences(context.getApplicationContext(), Constants.userId));
                            bookingParams.put("hotelId", hotelId);
                            bookingParams.put("catId", catIdList.get(position));
                            bookingParams.put("catType", "banquet");
                            bookingParams.put("startDate", selectedCheckInDate);
                            bookingParams.put("endDate", selectedCheckOutDate);
                            bookingParams.put("eventType", eventTypeET.getText().toString());

                            Log.e("Banquet Booking Params", bookingParams.toString());

                            bookingApi();

                        }


                    } else {
                        context.startActivity(new Intent(context.getApplicationContext(), Login.class));
                        context.finish();
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

        final ProgressDialog pd = new ProgressDialog(context);
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

                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.success_alert);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);

                            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/BalooBhai-Regular.ttf");

                            successHead.setTypeface(custom_font);
                            successMessage.setTypeface(custom_font);

                            successMessage.setText(object.getString("message"));

                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.finish();
                                }
                            });

                            dialog.show();

                        } else {

                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.success_alert);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            ImageView successImage = (ImageView) dialog.findViewById(R.id.successAlert_image);
                            TextView successHead = (TextView) dialog.findViewById(R.id.successAlert_header);
                            TextView successMessage = (TextView) dialog.findViewById(R.id.successAlert_message);
                            Button dialogButton = (Button) dialog.findViewById(R.id.successAlert_button);

                            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/BalooBhai-Regular.ttf");

                            successHead.setTypeface(custom_font);
                            successMessage.setTypeface(custom_font);

                            successHead.setText("Error!");
                            successMessage.setText(object.getString("message"));
                            successImage.setImageDrawable(context.getResources().getDrawable(R.drawable.img_fail));
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
                    Toast.makeText(context.getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(context, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams(){

                return bookingParams;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}