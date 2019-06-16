package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class FeaturedVideoAdapter extends RecyclerView.Adapter<FeaturedVideoAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> videoUrlList;
    private ArrayList<String> thumbUrlList;

    public FeaturedVideoAdapter(Activity mainActivity, ArrayList<String> videoUrl, ArrayList<String> thumbUrl) {

        this.context = mainActivity;
        this.videoUrlList = videoUrl;
        this.thumbUrlList = thumbUrl;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbIV, playBtn;

        public MyViewHolder(View view) {
            super(view);

            thumbIV = (ImageView) view.findViewById(R.id.adapter_video_thumbView);
            playBtn = (ImageView) view.findViewById(R.id.adapter_video_playBtn);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_videos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Picasso.with(context).load(thumbUrlList.get(position)).fit().centerCrop().placeholder(R.drawable.ic_checkin_white).into(holder.thumbIV);

        Log.e("Thumb Url", thumbUrlList.get(position));

        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrlList.get(position)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return videoUrlList.size();
    }
}