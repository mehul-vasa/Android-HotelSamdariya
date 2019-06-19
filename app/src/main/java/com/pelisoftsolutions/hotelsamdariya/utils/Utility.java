package com.pelisoftsolutions.hotelsamdariya.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/* if add this class get error so add in build.gradle second(Module app)
  to below of this line of libray
  buildToolsVersion "23.0.3"
  useLibrary 'org.apache.http.legacy'
*/

public class Utility {

	public static Context appContext;
	private static String PREFERENCE="Samdariya";

	// for username string preferences
	public static void setSharedPreference(Context context, String name,
                                           String value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		// editor.clear();
		editor.putString(name, value);
		editor.commit();
	}
	
	// for username string preferences
	public static void setIntegerSharedPreference(Context context, String name,
                                                  int value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		// editor.clear();
		editor.putInt(name, value);
		editor.commit();
	}
	
	//Drawable
		public static void setDrawableSharedPreference(Context context, String name,
                                                       int value) {
			appContext = context;
			SharedPreferences settings = context
					.getSharedPreferences(PREFERENCE, 0);
			SharedPreferences.Editor editor = settings.edit();
			// editor.clear();
			editor.putInt(name, value);
			editor.commit();
		}

	public static String getSharedPreferences(Context context, String name) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFERENCE, 0);
		return settings.getString(name, "");
	}

	
	public static int getIngerSharedPreferences(Context context, String name) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFERENCE, 0);
		return settings.getInt(name, 1);
	}

	public static void setSharedPreferenceBoolean(Context context, String name,
                                                  boolean value) {
		appContext = context;
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	public static boolean getSharedPreferencesBoolean(Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
		return settings.getBoolean(name, false);
	}


    public static void clearPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.clear();
        editor.commit();
    }

	public static double roundTwoDecimals(double d) {
	     DecimalFormat twoDForm = new DecimalFormat("#.##");
	     return Double.valueOf(twoDForm.format(d));
	}

	public static String parseDate(String orignalFormat, String newFormat, String date) {
        String formattedDate;
        DateFormat targetFormat;
        DateFormat originalFormat = new SimpleDateFormat(orignalFormat, Locale.ENGLISH);
        try {
            targetFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        } catch (IllegalArgumentException IAE) {
            newFormat = newFormat.replace("Y", "y");
            Log.e("New Format", newFormat);
            targetFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        }

        try{
            Date newDate = originalFormat.parse(date);
            formattedDate = targetFormat.format(newDate);  // 20120821
        } catch (ParseException E) {
            formattedDate = "";
        }

        return  formattedDate;
    }

	public static String calculateTeriff(int rate, int roomQty, String checkInDate, String checkOutDate, TextView roomAmtTV, TextView taxLabelTV, TextView taxAmtTv, TextView totalAmtTV) {


		Date checkIn = convertStringToDate(checkInDate, "yyyy-MM-dd");
		Date checkOut = convertStringToDate(checkOutDate, "yyyy-MM-dd");
		Log.e("CHECK IN", checkIn+".");
        Log.e("CHECK OUT", checkOut+".");

        int days = printDifference(checkIn, checkOut);
        Log.e("days", days+"..");

		int taxPercentage = 0;
		if(rate < 2500) {
			taxPercentage = 12;
		} else {
			taxPercentage = 18;
		}

		int teriff = rate * roomQty;
		int roomTeriif = teriff * days;
		int taxAmount =  roomTeriif * taxPercentage / 100;
		int terffAfterTax = roomTeriif + taxAmount;

		Log.e("Data", "Teriff : "+teriff + ", roomTerrif : " + roomTeriif);

		roomAmtTV .setText("Rs."+roomTeriif+"/-");
		taxLabelTV.setText("( CGST : "+taxPercentage/2+"%, SGST : +" + taxPercentage/2 + ")");
		taxAmtTv .setText("Rs."+taxAmount+"/-");
		totalAmtTV .setText("Rs."+terffAfterTax+"/-");

		return "Book Now For Rs." + terffAfterTax + "/-";
	}

	public static Date convertStringToDate (String dateToConvert, String dateFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date d = sdf.parse(dateToConvert);
            return d;
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
            return null;
        }

	}

    public static int printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;



        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return (int)elapsedDays;

    }

}// final class ends here

