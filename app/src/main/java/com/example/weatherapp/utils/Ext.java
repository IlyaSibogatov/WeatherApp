package com.example.weatherapp.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Ext {

    public static final String API_KEY = "3bc20e45e50441a185c175726232711";
    public static final String BASE_URL = "http://api.weatherapi.com/v1/";
    public static final int REQUEST_CODE = 100;

    public static class WeatherRequest {
        public static String lang = "ru";
        public static String daysCount = "5";
    }

    public enum WEATHER_STATUS{
        ERROR, SUCCESS, LOADING, SHOW_LATEST
    };

    public static String smallFormatedDate(String date_s) {
        Locale locale = new Locale("ru", "RU");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dt1 = new SimpleDateFormat("d MMM", locale);
        String result = "";
        try {
            Date date = dt.parse(date_s);
            result = dt1.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String formatedDate(String date_s) {
        Locale locale = new Locale("ru", "RU");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dt1 = new SimpleDateFormat("d MMM HH:mm", locale);
        String result = "";
        try {
            Date date = dt.parse(date_s);
            result = dt1.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}