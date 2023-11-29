package com.example.weatherapp.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "current_table")
public class CurrentWeatherEntity {
    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "icon")
    public String icon;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "temp")
    public Double temp;
    @ColumnInfo(name = "temp_feels")
    public Double temp_feels;
    @PrimaryKey @NotNull
    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "uv_c")
    public Double uv_c;
    @ColumnInfo(name = "humidity")
    public Double humidity;
    @ColumnInfo(name = "wind_speed")
    public Double wind_speed;
    @ColumnInfo(name = "wind_direction")
    public String wind_direction;
    @ColumnInfo(name = "pressure")
    public Double pressure;
    public CurrentWeatherEntity() { }
    public CurrentWeatherEntity(
            String location, String icon, String text, Double temp_c, Double feelslike_c,
            String localtime, Double uv, Double humidity, Double wind_kph, String wind_dir,
            Double pressure_mb) {
        this.location = location;
        this.icon = icon;
        this.description = text;
        this.temp = temp_c;
        this.temp_feels = feelslike_c;
        this.date = localtime;
        this.uv_c = uv;
        this.humidity = humidity;
        this.wind_speed = wind_kph;
        this.wind_direction = wind_dir;
        this.pressure = pressure_mb;
    }
}
