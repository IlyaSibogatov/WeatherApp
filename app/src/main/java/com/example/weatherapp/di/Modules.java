package com.example.weatherapp.di;

import static com.example.weatherapp.utils.Ext.BASE_URL;
import android.content.Context;
import androidx.room.Room;
import com.example.weatherapp.data.database.MyDatabase;
import com.example.weatherapp.data.database.WeatherDao;
import com.example.weatherapp.data.services.WeatherService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class Modules {

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(
            HttpLoggingInterceptor loggingInterceptor
    ) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Gson providerGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public GsonConverterFactory providerGsonConvertFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor providerLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private <T> T providerService(
            OkHttpClient okHttpClient,
            GsonConverterFactory gsonConverterFactory,
            Class<T> clazz
    ) {
        return createRetrofit(okHttpClient, gsonConverterFactory).create(clazz);
    }

    private Retrofit createRetrofit(
            OkHttpClient okHttpClient,
            GsonConverterFactory gsonConverterFactory
    ) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public WeatherService provideCatalogService(
            OkHttpClient okHttpClient,
            GsonConverterFactory gsonConverterFactory
    ) {
        return providerService(okHttpClient, gsonConverterFactory, WeatherService.class);
    }

    @Provides
    public WeatherDao provideCartDao(MyDatabase myDatabase){
        return myDatabase.weatherDao();
    }

    @Provides
    @Singleton
    public MyDatabase provideMyDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(
                appContext,
                MyDatabase.class,
                "DataBase"
        ).build();
    }
}