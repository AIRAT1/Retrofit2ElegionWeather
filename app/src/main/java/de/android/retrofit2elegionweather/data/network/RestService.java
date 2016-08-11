package de.android.retrofit2elegionweather.data.network;

import de.android.retrofit2elegionweather.data.network.weathermodelres.Model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {
    @GET("weather")
    Call<Model> getWeatherReport(
            @Query("q") String name,
            @Query("mode") String mode,
            @Query("units") String units,
            @Query("lang") String lang,
            @Query("APPID") String appId);
}