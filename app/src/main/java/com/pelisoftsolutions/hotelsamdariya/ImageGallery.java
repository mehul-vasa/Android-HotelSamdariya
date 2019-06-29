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

        galleryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int i, long id) {
                Picasso.with(ImageGallery.this).load(imageUrl.get(i)).placeholder(R.drawable.logo).into(imgView);
                int imagePosition = i + 1;
                Toast.makeText(getApplicationContext(), "You have selected image = " + imageUrl.get(i), Toast.LENGTH_LONG).show();
            }
        });

    }

    public class myImageAdapter extends BaseAdapter {

        private Context mcontext;

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mgalleryView = new ImageView(mcontext);
            Picasso.with(mcontext).load(imageUrl.get(position)).placeholder(R.drawable.logo).into(mgalleryView);
//            .setImageResource(imageResource[position]);
            mgalleryView.setLayoutParams(new Gallery.LayoutParams(150, 150));
            mgalleryView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //imgView.setImageResource(R.drawable.image_border);
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
