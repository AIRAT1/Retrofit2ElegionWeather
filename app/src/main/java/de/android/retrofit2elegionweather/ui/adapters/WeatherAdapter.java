package de.android.retrofit2elegionweather.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.android.retrofit2elegionweather.R;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    List<String> shortWeather;

    public WeatherAdapter(List<String> shortWeather) {
        this.shortWeather = shortWeather;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_list, parent, false);
        return new WeatherViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.itemShortWeather.setText(shortWeather.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return shortWeather.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView itemShortWeather;
        public WeatherViewHolder(View itemView) {
            super(itemView);
            itemShortWeather = (TextView) itemView.findViewById(R.id.item_short_weather);
        }
    }
}
