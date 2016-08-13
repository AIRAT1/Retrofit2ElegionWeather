package de.android.retrofit2elegionweather.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.ui.views.AspectRatioImageView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    ArrayList<Object> weathers;

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_list, parent, false);
        return new WeatherViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        protected AspectRatioImageView aspectRatioImageView;
        protected TextView itemShortWeather;
        public WeatherViewHolder(View itemView) {
            super(itemView);

            aspectRatioImageView = (AspectRatioImageView) itemView.findViewById(R.id.aspect_ratio_image_view);
            itemShortWeather = (TextView) itemView.findViewById(R.id.item_short_weather);
        }
    }
}
