package com.example.weatherapp.ui.fragments;

import static com.example.weatherapp.utils.Ext.REQUEST_CODE;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.ERROR;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.LOADING;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.SHOW_LATEST;
import static com.example.weatherapp.utils.Ext.WEATHER_STATUS.SUCCESS;
import static com.example.weatherapp.utils.Ext.formatedDate;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherapp.utils.Ext;
import com.example.weatherapp.R;
import com.example.weatherapp.ui.viewmodels.MainViewModel;
import com.example.weatherapp.ui.adapters.ForecastAdapter;
import com.example.weatherapp.data.database.entities.CurrentWeatherEntity;
import com.example.weatherapp.data.database.entities.ForecastEntity;
import com.example.weatherapp.databinding.FragmentMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentMainBinding binding;
    private ForecastAdapter forecastAdapter;
    private FusedLocationProviderClient locationClient;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireContext();
        forecastAdapter = new ForecastAdapter();
        locationClient = LocationServices.getFusedLocationProviderClient(context);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }
        binding.forecastRv.setAdapter(forecastAdapter);
        setObservables();
    }

    private void setObservables() {
        viewModel.showMain.observe(getViewLifecycleOwner(), new Observer<Ext.WEATHER_STATUS>() {
            @Override
            public void onChanged(Ext.WEATHER_STATUS status) {
                if (status == LOADING) {
                    binding.infoTv.setVisibility(View.GONE);
                    binding.mainScrollView.setVisibility(View.GONE);
                }
                if (status == ERROR) {
                    binding.infoTv.setVisibility(View.VISIBLE);
                    binding.mainScrollView.setVisibility(View.GONE);
                }
                if (status == SUCCESS) {
                    binding.infoTv.setVisibility(View.GONE);
                    binding.mainScrollView.setVisibility(View.VISIBLE);
                }
                if (status == SHOW_LATEST) {
                    binding.infoTv.setVisibility(View.GONE);
                    binding.mainScrollView.setVisibility(View.VISIBLE);
                    Toast.makeText(context, getResources().getString(R.string.main_error_label), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.weatherData.observe(getViewLifecycleOwner(), new Observer<CurrentWeatherEntity>() {
                    @Override
                    public void onChanged(CurrentWeatherEntity data) {
                        binding.currentLocation.setText(
                                getResources()
                                        .getString(
                                                R.string.location_label,
                                                data.location
                                        )
                        );
                        String newUrl = "http:" + data.icon;
                        Glide
                                .with(context)
                                .load(newUrl)
                                .centerCrop()
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                .into(binding.weatherImage);
                        binding.weatherDescription.setText(data.description);
                        binding.currentTempText.setText(getResources().getString(
                                        R.string.temp_label,
                                        data.temp.toString(),
                                        data.temp_feels.toString()
                                )
                        );
                        binding.lastUpdateText.setText(
                                formatedDate(data.date)
                        );
                        binding.ultravioletText.setText(getResources().getString(
                                        R.string.uv_label,
                                        data.uv_c.toString()
                                )
                        );
                        binding.humidityText.setText(getResources().getString(
                                        R.string.humidity_label,
                                        data.humidity.toString()
                                )
                        );
                        binding.windText.setText(getResources().getString(
                                        R.string.wind_label,
                                        data.wind_speed.toString(),
                                        data.wind_direction
                                )
                        );
                        binding.pressureText.setText(getResources().getString(
                                        R.string.pressure_label,
                                        data.pressure.toString()
                                )
                        );
                        LinearLayoutManager manager = new LinearLayoutManager(context) {
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        binding.forecastRv.setLayoutManager(manager);

                        if (viewModel.showMain.getValue() != SUCCESS) viewModel.setVisibility(SUCCESS);
                    }
                }
        );

        viewModel.forecastsData.observe(getViewLifecycleOwner(), new Observer<List<ForecastEntity>>() {
                    @Override
                    public void onChanged(List<ForecastEntity> forecastEntities) {
                        forecastAdapter.setList(forecastEntities);
                    }
                }
        );
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(context, new Locale("ru", "RU"));
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            viewModel.getCurrent(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                            viewModel.checkLatestData();
                        }
                    } else viewModel.checkLatestData();
                }
            });
        }
    }

    private void askPermissions() {
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                getLastLocation();
            } else viewModel.checkLatestData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLastLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}