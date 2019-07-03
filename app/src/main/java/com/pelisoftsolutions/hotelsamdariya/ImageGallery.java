package com.pelisoftsolutions.hotelsamdariya;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageGallery extends Activity {

    Gallery galleryView;
    ImageView imgView;

    ArrayList<String> imageUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        imgView = (ImageView) findViewById(R.id.imageGallery_selectedImageIV);
        galleryView = (Gallery) findViewById(R.id.imageGallery_galleryView);

        imageUrl = getIntent().getStringArrayListExtra("imageUrlList");

        Log.e("image Url ", imageUrl.toString());

        galleryView.setAdapter(new myImageAdapter(this));

        Picasso.with(ImageGallery.this).load(imageUrl.get(0)).placeholder(R.drawable.logo).into(imgView);

        galleryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int i, long id) {
                Picasso.with(ImageGallery.this).load(imageUrl.get(i)).placeholder(R.drawable.logo).into(imgView);
            }
        });

    }

    public class myImageAdapter extends BaseAdapter {

        private Context mcontext;

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mgalleryView = new ImageView(mcontext);
            Picasso.with(mcontext).load(imageUrl.get(position)).placeholder(R.drawable.logo).into(mgalleryView);
            mgalleryView.setLayoutParams(new Gallery.LayoutParams(150, 150));
            mgalleryView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mgalleryView.setPadding(3, 3, 3, 3);

            return mgalleryView;
        }

        public myImageAdapter(Context context) {
            mcontext = context;
        }

        public int getCount() {
            return imageUrl.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
    }

}
