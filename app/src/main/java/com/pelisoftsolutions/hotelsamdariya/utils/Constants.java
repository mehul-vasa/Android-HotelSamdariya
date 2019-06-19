package com.pelisoftsolutions.hotelsamdariya.utils;

import android.util.Log;
import android.widget.Toast;

public class Constants {

    public static final Boolean isDevelopmentModeOn = true;


//    private static final String domain = "http://pelisoftsolutions.com/demo/samdariya/api/";
    private static final String domain = "http://192.168.1.11/samdariya/api/";



    //API URLS
    public static final String loginUrl = domain + "login_api.php";
    public static final String signUpUrl = domain + "signUp_api.php";
    public static final String forgotPasswordUrl = domain + "forgotPassword_api.php";
    public static final String getHotelListUrl = domain + "getHotels_api.php";
    public static final String getOfferListUrl = domain + "getOffers_api.php";
    public static final String getHotelDetailsUrl = domain + "getHotelDetails_api.php";
    public static final String getTestimonialsUrl = domain + "getTestimonials_api.php";
    public static final String getCategoryDetailUrl = domain + "getCategoryDetail_api.php";
    public static final String getRoomCategoriesUrl = domain + "getRoomCategories_api.php";
    public static final String bookingUrl = domain + "book_api.php";
    public static final String getBanquetsUrl = domain + "getBanquets_api.php";

    //SHARED PREFERENCE KEYS
    public static final String loginStatus = "isUserLoggedIn";
    public static final String userId = "userId";
    public static final String userName = "userName";
    public static final String userContact = "userContact";
    public static final String userEmail = "userEmail";
    public static final String hotelId = "hotelId";
    public static final String bookingParams = "bookingParams";
    public static final String source = "source";
    public static final String sectionId = "sectionId";
    public static final String studentId = "studentId";


}


