package com.example.weatherapp.data.services;

import com.example.weatherapp.data.models.CurrentResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("current.json")
    public Observable<CurrentResponse> realtimeWeather(
            @Query("q") String query,
            @Query("key") String apiKey,
            @Query("lang") String language
    );

    @GET("forecast.json")
    public Observable<CurrentResponse> weatherForecast(
            @Query("q") String query,
            @Query("key") String apiKey,
            @Query("lang") String language,
            @Query("days") String days
    );
}
