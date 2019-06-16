package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.media.Rating;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pelisoftsolutions.hotelsamdariya.HotelPage;
import com.pelisoftsolutions.hotelsamdariya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TestimonialsAdapter extends RecyclerView.Adapter<TestimonialsAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> testimonialIdList;
    private ArrayList<String> testimonialNameList;
    private ArrayList<String> testimonialRatingList;
    private ArrayList<String> testimonialCommentList;

    public TestimonialsAdapter(Activity hotelPage, ArrayList<String> testimonialIdList, ArrayList<String> testimonialNameList,
                               ArrayList<String> testimonialRatingList, ArrayList<String> testimonialCommentList) {

        this.context = hotelPage;
        this.testimonialIdList = testimonialIdList;
        this.testimonialNameList = testimonialNameList;
        this.testimonialRatingList = testimonialRatingList;
        this.testimonialCommentList = testimonialCommentList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView nameTV, commentTV;
        public RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);

            nameTV = (TextView) view.findViewById(R.id.adapter_testimonial_nameTV);
            commentTV = (TextView) view.findViewById(R.id.adapter_testimonial_commentTV);
            ratingBar = (RatingBar) view.findViewById(R.id.adapter_testimonial_ratingBar);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_testimonials, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.nameTV.setText(testimonialNameList.get(position));
        holder.commentTV.setText(testimonialCommentList.get(position));
        holder.ratingBar.setRating(Float.parseFloat(testimonialRatingList.get(position)));

    }

    @Override
    public int getItemCount() {
        return testimonialIdList.size();
    }
}