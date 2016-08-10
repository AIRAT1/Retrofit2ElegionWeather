package de.android.retrofit2elegionweather;

import de.android.retrofit2elegionweather.POJO.Model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestInterface {
//    @GET("weather?q=" + "Berlin, de" + "&mode=json&units=metric&lang=de&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY)
//    Call<Model> getWeatherReport();
    @GET("weather")
    Call<Model> getWeatherReport(
            @Query("q") String name,
            @Query("mode") String mode,
            @Query("units") String units,
            @Query("lang") String lang,
            @Query("APPID") String appId);
}