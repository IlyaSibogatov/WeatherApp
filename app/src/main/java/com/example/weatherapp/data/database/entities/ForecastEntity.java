package com.example.weatherapp.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "forecast_table")
public class ForecastEntity {
    @PrimaryKey @NotNull
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "avgtemp_c")
    public Double avgT;
    @ColumnInfo(name = "avghumidity")
    public Double avgH;
    @ColumnInfo(name = "maxwind_kph")
    public Double maxWind;
    @ColumnInfo(name = "icon")
    public String icon;

    public ForecastEntity() { }

    public ForecastEntity(
            String date,
            Double avgtemp_c,
            Double avghumidity,
            Double maxwind_kph,
            String icon
    ) {
        this.date = date;
        this.avgT = avgtemp_c;
        this.avgH = avghumidity;
        this.maxWind = maxwind_kph;
        this.icon = icon;
    }
}
