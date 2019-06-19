package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.pelisoftsolutions.hotelsamdariya.HotelRoomPage;
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
import java.util.List;
import java.util.Map;

public class AminitiesAdapter extends RecyclerView.Adapter<AminitiesAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> aminitiesList;

    public AminitiesAdapter(Activity hotelRoomPage, ArrayList<String> aminitiesList) {
        this.context = hotelRoomPage;
        this.aminitiesList = aminitiesList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView nameTV;

        public MyViewHolder(View view) {
            super(view);

            nameTV = (TextView) view.findViewById(R.id.aminitiesAdapter_nameTV);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_aminities, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.nameTV.setText(aminitiesList.get(position));
        Log.e("aminitiesList", aminitiesList.get(position));

    }

    @Override
    public int getItemCount() {
        return aminitiesList.size();
    }





}