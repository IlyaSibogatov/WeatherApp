package com.example.weatherapp.adapters;

import static com.example.weatherapp.Ext.smallFormatedDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherapp.R;
import com.example.weatherapp.data.database.entities.ForecastEntity;
import com.example.weatherapp.databinding.ForecastItemBinding;
import java.util.Collections;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    List<ForecastEntity> forecasts = Collections.emptyList();

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<ForecastEntity> list) {
        forecasts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ForecastItemBinding itemView = ForecastItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        ForecastEntity item = forecasts.get(position);
        String newUrl = "http:" + item.icon;
        Glide
                .with(holder.itemView.getContext())
                .load(newUrl)
                .centerCrop()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.binding.weatherIcon);
        holder.binding.dateTv.setText(smallFormatedDate(item.date));
        holder.binding.tempTv.setText(
                context.getString(R.string.small_temp_label, item.avgT.toString())
        );
        holder.binding.humidityTv.setText(item.avgH + "%");
        holder.binding.windTv.setText(
                context.getString(R.string.small_wind_label, item.maxWind.toString())
        );
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ForecastItemBinding binding;

        public ViewHolder(ForecastItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    @Override
    public int getItemCount() {
        if (forecasts == null) return 0;
        else return forecasts.size();
    }
}
