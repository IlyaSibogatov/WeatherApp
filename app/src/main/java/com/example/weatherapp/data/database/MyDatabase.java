package com.example.weatherapp.data.database;

import androidx.room.RoomDatabase;
import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;

@androidx.room.Database(entities = {ForecastEntity.class, CurrentWeatherEntity.class}, version = 1)

public abstract class MyDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
