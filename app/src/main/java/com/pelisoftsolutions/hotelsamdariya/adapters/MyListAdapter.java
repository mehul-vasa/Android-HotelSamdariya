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
import com.pelisoftsolutions.hotelsamdariya.HotelPage;
import com.pelisoftsolutions.hotelsamdariya.ListActivity;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> {

private Activity context;
private ArrayList<String> idList;
private ArrayList<String> nameList;
private ArrayList<String> imageList;
private ArrayList<String> hotelIdList;
private String source;

    public MyListAdapter(Activity listActivity, ArrayList<String> idList, ArrayList<String> nameList, ArrayList<String> imageList, ArrayList<String> hotelIdList, String source) {

        this.context = listActivity;
        this.idList = idList;
        this.nameList = nameList;
        this.imageList = imageList;
        this.hotelIdList = hotelIdList;
        this.source = source;

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
                .inflate(R.layout.adapter_listview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.hotelNameTV.setText(nameList.get(position));
        Picasso.with(context).load(imageList.get(position)).fit().into(holder.hotelImageIV);


        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(source.equalsIgnoreCase("hotel")) {

                    Intent asd = new Intent(context, HotelPage.class);
                    asd.putExtra(Constants.hotelId, hotelIdList.get(position));
                    context.startActivity(asd);

                } else if (source.equalsIgnoreCase("banquet")) {

                    Intent asd = new Intent(context, BanuquetDetail.class);
                    asd.putExtra("source", "banquet");
                    asd.putExtra("hotelId", hotelIdList.get(position));
                    asd.putExtra("id", idList.get(position));
                    context.startActivity(asd);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return idList.size();
    }
}