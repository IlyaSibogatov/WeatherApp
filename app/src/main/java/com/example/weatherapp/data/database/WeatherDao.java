package com.example.weatherapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface WeatherDao {
    @Insert
    Completable insertCurrentWeather(CurrentWeatherEntity weather);

    @Insert
    Completable insertForecasts(List<ForecastEntity> forecasts);

    @Query("SELECT * FROM current_table")
    Observable<List<CurrentWeatherEntity>> getCurrentWeather();

    @Query("SELECT * FROM forecast_table")
    Observable<List<ForecastEntity>> getForecasts();

    @Query("DELETE FROM current_table")
    Completable deleteCurrent();

    @Query("DELETE FROM forecast_table")
    Completable deleteForecasts();
}