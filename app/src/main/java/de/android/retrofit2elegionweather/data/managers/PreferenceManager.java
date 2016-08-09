package de.android.retrofit2elegionweather.data.managers;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.android.retrofit2elegionweather.utils.ConstantManager;
import de.android.retrofit2elegionweather.utils.Retrofit2ElegionWeatherApplication;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private static final String[] WEATHER_FIELDS = {
            ConstantManager.KEY_CITY,
            ConstantManager.KEY_TEMPERATURE,
            ConstantManager.KEY_STATUS,
            ConstantManager.KEY_HUMIDITY,
            ConstantManager.KEY_PRESSURE,
            ConstantManager.KEY_TIME
    };

    public PreferenceManager() {
        this.sharedPreferences = Retrofit2ElegionWeatherApplication.getSharedPreferences();
    }
    public void saveWeatherData(List<String> weatherFields) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i <WEATHER_FIELDS.length; i++) {
            editor.putString(WEATHER_FIELDS[i], weatherFields.get(i));
        }
        editor.apply();
    }
    public List<String> loadWeatherData() {
        List<String> weatherFields = new ArrayList<>();
        for (int i = 0; i < WEATHER_FIELDS.length; i++) {
            weatherFields.add(sharedPreferences.getString(WEATHER_FIELDS[i], "null"));
        }
        return weatherFields;
    }
}
