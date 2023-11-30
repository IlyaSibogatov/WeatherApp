package com.example.weatherapp.ui.viewmodels;

import static com.example.weatherapp.utils.Ext.API_KEY;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.ERROR;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.SHOW_LATEST;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.utils.Ext;
import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;
import com.example.weatherapp.data.repositories.WeatherRepository;
import com.example.weatherapp.data.services.WeatherService;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final WeatherService service;
    private final WeatherRepository repository;
    private final MutableLiveData<CurrentWeatherEntity> _weatherData = new MutableLiveData<>();
    public LiveData<CurrentWeatherEntity> weatherData = _weatherData;
    private final MutableLiveData<List<ForecastEntity>> _forecastsData = new MutableLiveData<>();
    public LiveData<List<ForecastEntity>> forecastsData = _forecastsData;
    private final MutableLiveData<Ext.WEATHER_STATUS> _showMain = new MutableLiveData<>(Ext.WEATHER_STATUS.LOADING);
    public LiveData<Ext.WEATHER_STATUS> showMain = _showMain;

    @Inject
    public MainViewModel(WeatherService service, WeatherRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    public void getCurrent(Double lat, Double lon) {
        service.weatherForecast(
                        lat.toString() + "," + lon.toString(),
                        API_KEY,
                        Ext.WeatherRequest.lang,
                        Ext.WeatherRequest.daysCount
                )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(it -> {
                            repository.removeCurrent();
                            repository.removeForecasts();

                            CurrentWeatherEntity currentEntity = new CurrentWeatherEntity(
                                    it.location.country + ", " + it.location.name,
                                    it.current.condition.icon,
                                    it.current.condition.text,
                                    it.current.temp_c,
                                    it.current.feelslike_c,
                                    it.location.localtime,
                                    it.current.uv,
                                    it.current.humidity,
                                    it.current.wind_kph,
                                    it.current.wind_dir,
                                    it.current.pressure_mb
                            );
                            _weatherData.postValue(currentEntity);
                            repository.insertCurrent(currentEntity);

                            List<ForecastEntity> forecasts = it.forecast.forecastday.stream().map(forecastday ->
                                    new ForecastEntity(
                                            forecastday.date,
                                            forecastday.day.avgtemp_c,
                                            forecastday.day.avghumidity,
                                            forecastday.day.maxwind_kph,
                                            forecastday.day.condition.icon
                                    )
                            ).collect(Collectors.toList());
                            _forecastsData.postValue(forecasts);
                            repository.insertForecasts(forecasts)
                                    .subscribeOn(Schedulers.io());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void checkLatestData() {
        repository.getCurrentWeather()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    if (data.isEmpty()) setVisibility(ERROR);
                    else {
                        _weatherData.postValue(data.get(0));
                        repository.getForecasts()
                                .observeOn(Schedulers.io())
                                .subscribe(_forecastsData::postValue);
                        setVisibility(SHOW_LATEST);
                    }
                });
    }

    public void setVisibility(Ext.WEATHER_STATUS value) {
        _showMain.postValue(value);
    }
}