package de.android.retrofit2elegionweather.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Retrofit2ElegionWeatherApplication extends Application{
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
