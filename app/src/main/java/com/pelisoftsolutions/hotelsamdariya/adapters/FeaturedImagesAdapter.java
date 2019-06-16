package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pelisoftsolutions.hotelsamdariya.HotelPage;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeaturedImagesAdapter extends RecyclerView.Adapter<FeaturedImagesAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> hotelIdList;
    private ArrayList<String> hotelNameList;
    private ArrayList<String> hotelImageList;

    public FeaturedImagesAdapter(Activity mainActivity, ArrayList<String> hotelIdList, ArrayList<String> hotelNameList, ArrayList<String> hotelImageList) {

        this.context = mainActivity;
        this.hotelIdList = hotelIdList;
        this.hotelNameList = hotelNameList;
        this.hotelImageList = hotelImageList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hotelNameTV;
        public ImageView hotelImageIV;
        public CardView viewContainer;

        public MyViewHolder(View view) {
            super(view);

            hotelNameTV = (TextView) view.findViewById(R.id.adapter_dashboardImage_nameTV);
            hotelImageIV = (ImageView) view.findViewById(R.id.adapter_dashboardImage_imageview);
            viewContainer = (CardView) view.findViewById(R.id.adapter_dashboardImage_container);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.hotelNameTV.setText(hotelNameList.get(position));
        Picasso.with(context).load(hotelImageList.get(position)).fit().into(holder.hotelImageIV);

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent asd = new Intent(context, HotelPage.class);
                asd.putExtra(Constants.hotelId, hotelIdList.get(position));
                context.startActivity(asd);
            }
        });




    }

    @Override
    public int getItemCount() {
        return hotelIdList.size();
    }
}