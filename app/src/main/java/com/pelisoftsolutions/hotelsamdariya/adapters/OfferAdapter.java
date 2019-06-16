package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pelisoftsolutions.hotelsamdariya.BaseActivity;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> offerIdList;
    private ArrayList<String> offerTitleList;
    private ArrayList<String> offerImageList;

    public OfferAdapter(BaseActivity mainActivity, ArrayList<String> offerIdList, ArrayList<String> offerTitleList, ArrayList<String> offerImageList) {

        this.context = mainActivity;
        this.offerIdList = offerIdList;
        this.offerTitleList = offerTitleList;
        this.offerImageList = offerImageList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView offerTitleTV;
        public ImageView offerImageIV;

        public MyViewHolder(View view) {
            super(view);

            offerTitleTV = (TextView) view.findViewById(R.id.adapter_offer_titleTV);
            offerImageIV = (ImageView) view.findViewById(R.id.adapter_offer_offerIV);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_offers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.offerTitleTV.setText(offerTitleList.get(position));
        Picasso.with(context).load(offerImageList.get(position)).fit().centerInside().into(holder.offerImageIV);
    }

    @Override
    public int getItemCount() {
        return offerIdList.size();
    }
}