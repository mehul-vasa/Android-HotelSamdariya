package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pelisoftsolutions.hotelsamdariya.ImageGallery;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> imageUrlList;

    public GalleryAdapter(Activity banuquetDetail, ArrayList<String> imagePathList) {
        this.context = banuquetDetail;
        this.imageUrlList = imagePathList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageview;

        public MyViewHolder(View view) {
            super(view);

            imageview = (ImageView) view.findViewById(R.id.galleryAdapter_imageview);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(context).load(imageUrlList.get(position)).fit().into(holder.imageview);

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent asd = new Intent(context, ImageGallery.class);
                asd.putStringArrayListExtra("imageUrlList", imageUrlList);
                context.startActivity(asd);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }
}