package de.android.retrofit2elegionweather;

import de.android.retrofit2elegionweather.POJO.Model;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestInterface {
    @GET("weather?q=Berlin,de&mode=json&units=metric&lang=de&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY)
    Call<Model> getWeatherReport();
}