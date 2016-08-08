package de.android.retrofit2elegionweather;

import de.android.retrofit2elegionweather.POJO.Model;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestInterface {
    @GET("data/2.5/weather?q=Berlin,de&APPID=a67b395621fb7f28f3896da2171e6a40")
    Call<Model> getWeatherReport();
}
