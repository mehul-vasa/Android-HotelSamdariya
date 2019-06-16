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

import com.pelisoftsolutions.hotelsamdariya.BanuquetDetail;
import com.pelisoftsolutions.hotelsamdariya.BaseActivity;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BanquetAdapter extends RecyclerView.Adapter<BanquetAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> banquetIdList;
    private ArrayList<String> hotelIdList;
    private ArrayList<String> banquetNameList;
    private ArrayList<String> banquetImageList;


    public BanquetAdapter(BaseActivity mainActivity, ArrayList<String> banquetIdList, ArrayList<String> banquetNameList, ArrayList<String> banquetImageList, ArrayList<String> banquetHotelIdList) {

        this.context = mainActivity;
        this.banquetIdList = banquetIdList;
        this.banquetNameList = banquetNameList;
        this.banquetImageList = banquetImageList;
        this.hotelIdList = banquetHotelIdList;
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

        holder.hotelNameTV.setText(banquetNameList.get(position));
        Picasso.with(context).load(banquetImageList.get(position)).fit().into(holder.hotelImageIV);

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent asd = new Intent(context, BanuquetDetail.class);
                asd.putExtra("source", "banquet");
                asd.putExtra("hotelId", hotelIdList.get(position));
                asd.putExtra("id", banquetIdList.get(position));
                context.startActivity(asd);
            }
        });




    }

    @Override
    public int getItemCount() {
        return hotelIdList.size();
    }
}