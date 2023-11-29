package com.example.weatherapp.di;

import com.example.weatherapp.data.repositories.WeatherRepository;
import com.example.weatherapp.data.repositories.WeatherRepositoryImpl;
import javax.inject.Singleton;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract WeatherRepository provideRecipeRepository(
            WeatherRepositoryImpl weatherRepository
    );
}
