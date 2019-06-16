package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pelisoftsolutions.hotelsamdariya.R;
import com.pelisoftsolutions.hotelsamdariya.utils.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryImagesViewPagerAdapter extends RecyclingPagerAdapter {

    private Activity context;
    private ArrayList<String> imageUrlList;
    private boolean       isInfiniteLoop;



    public CategoryImagesViewPagerAdapter(Activity categoryDetails, ArrayList<String> imageUrlList) {

        this.context = categoryDetails;
        this.imageUrlList = imageUrlList;
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageUrlList.size();
    }

    private int getPosition(int position) {
        return isInfiniteLoop ? position % imageUrlList.size() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_categories_viewpager_images, parent, false);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.adapter_dashboardImage_imageview);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Picasso.with(context).load(imageUrlList.get(getPosition(position))).fit().centerCrop().into(viewHolder.imageView);

        return convertView;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }


    public CategoryImagesViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

}










//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView hotelNameTV;
//        public ImageView hotelImageIV;
//        public CardView viewContainer;
//
//        public MyViewHolder(View view) {
//            super(view);
//
//            hotelNameTV = (TextView) view.findViewById(R.id.adapter_dashboardImage_nameTV);
//            hotelImageIV = (ImageView) view.findViewById(R.id.adapter_dashboardImage_imageview);
//            viewContainer = (CardView) view.findViewById(R.id.adapter_dashboardImage_container);
//
//        }
//    }
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_images, parent, false);
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//
//        holder.hotelNameTV.setText(hotelNameList.get(position));
//        Picasso.with(context).load(hotelImageList.get(position)).fit().into(holder.hotelImageIV);
//
//        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent asd = new Intent(context, HotelPage.class);
//                asd.putExtra(Constants.hotelId, hotelIdList.get(position));
//                context.startActivity(asd);
//            }
//        });
//
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return hotelIdList.size();
//    }
//}