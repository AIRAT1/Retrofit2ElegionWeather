package de.android.retrofit2elegionweather.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.android.retrofit2elegionweather.POJO.Model;
import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.RestInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.txt_city) TextView txtCity;
    @BindView(R.id.txt_temperature) TextView txtTemperature;
    @BindView(R.id.txt_status) TextView txtStatus;
    @BindView(R.id.txt_humidity) TextView txtHumidity;
    @BindView(R.id.txt_pressure) TextView txtPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getReport();
    }

    private void getReport() {
        showProgress();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestInterface service = retrofit.create(RestInterface.class);
        Call<Model> call = service.getWeatherReport();

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                hideProgress();
                try {
                    String city = response.body().getName();
                    String temperature = String.valueOf(response.body().getMain().getTemp());
                    String status = response.body().getWeather().get(0).getDescription();
                    String humidity = String.valueOf(response.body().getMain().getHumidity());
                    String pressure = String.valueOf(response.body().getMain().getPressure());

                    txtCity.setText("City: " + city);
                    txtTemperature.setText("Temperature: " + temperature + " C");
                    txtStatus.setText("Status: " + status);
                    txtHumidity.setText("Humidity: " + humidity + " %");
                    txtPressure.setText("Pressure: " + pressure + " kPa");
                }catch (NullPointerException e) {
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                hideProgress();
                Log.d(TAG, t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
