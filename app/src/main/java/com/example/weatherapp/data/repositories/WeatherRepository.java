package com.example.weatherapp.data.repositories;

import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface WeatherRepository {
    Completable insertCurrent(CurrentWeatherEntity current);

    Completable insertForecasts(List<ForecastEntity> forecastList);

    Observable<List<CurrentWeatherEntity>> getCurrentWeather();

    Observable<List<ForecastEntity>> getForecasts();

    Completable removeCurrent();

    Completable removeForecasts();
}