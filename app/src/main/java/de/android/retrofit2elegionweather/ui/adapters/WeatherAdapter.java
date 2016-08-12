package de.android.retrofit2elegionweather.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.ui.views.AspectRatioImageView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        protected AspectRatioImageView germanColors;
        protected TextView itemCityName, itemCityTemperature;
        public WeatherViewHolder(View itemView) {
            super(itemView);

            germanColors = (AspectRatioImageView) itemView.findViewById(R.id.german_colors);
            itemCityName = (TextView) itemView.findViewById(R.id.item_city_name);
            itemCityTemperature = (TextView) itemView.findViewById(R.id.item_temperature);
        }
    }
}
