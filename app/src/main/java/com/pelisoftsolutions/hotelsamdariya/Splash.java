package com.pelisoftsolutions.hotelsamdariya;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.pelisoftsolutions.hotelsamdariya.utils.Constants;
import com.pelisoftsolutions.hotelsamdariya.utils.Utility;

public class Splash extends AppCompatActivity {

    final int SPLASH_DISPLAY_LENGTH = 1000;
    boolean loginStatus;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        try {
            loginStatus  = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.loginStatus);
            userId = Utility.getSharedPreferences(getApplicationContext(), Constants.userId);
        } catch (NullPointerException e) {
            loginStatus = false;
            userId = "";
            Log.e("STATUS", "NULL");
        }

        Log.e("Login Status", loginStatus+"");

        if(loginStatus || userId.equalsIgnoreCase("0") ) {

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, Dashboard.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        } else {

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, Login.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }


    }
}
