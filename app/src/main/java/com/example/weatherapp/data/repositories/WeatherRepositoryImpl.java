package com.example.weatherapp.data.repositories;

import com.example.weatherapp.data.database.WeatherDao;
import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class WeatherRepositoryImpl implements WeatherRepository {

    private final WeatherDao dao;

    @Inject
    public WeatherRepositoryImpl(WeatherDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable insertCurrent(CurrentWeatherEntity current) {
        return dao.insertCurrentWeather(current);
    }

    @Override
    public Completable insertForecasts(List<ForecastEntity> forecastList) {
        return dao.insertForecasts(forecastList);
    }

    @Override
    public Observable<List<CurrentWeatherEntity>> getCurrentWeather() {
        return dao.getCurrentWeather();
    }

    @Override
    public Observable<List<ForecastEntity>> getForecasts() {
        return dao.getForecasts();
    }

    @Override
    public Completable removeCurrent() {
        return dao.deleteCurrent();
    }

    @Override
    public Completable removeForecasts() {
        return dao.deleteForecasts();
    }
}
