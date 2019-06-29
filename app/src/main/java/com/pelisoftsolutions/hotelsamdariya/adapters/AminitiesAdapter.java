package com.pelisoftsolutions.hotelsamdariya.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pelisoftsolutions.hotelsamdariya.R;

import java.util.ArrayList;

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