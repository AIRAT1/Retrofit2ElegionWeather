package de.android.retrofit2elegionweather.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.android.retrofit2elegionweather.POJO.Model;
import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.RestInterface;
import de.android.retrofit2elegionweather.data.managers.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DataManager dataManager;
    private List<TextView> weatherInfoViews;

    @BindView(R.id.txt_city) TextView txtCity;
    @BindView(R.id.txt_temperature) TextView txtTemperature;
    @BindView(R.id.txt_status) TextView txtStatus;
    @BindView(R.id.txt_humidity) TextView txtHumidity;
    @BindView(R.id.txt_pressure) TextView txtPressure;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dataManager = DataManager.getInstance();

        initWeatherInfoViews();

        getReport();
    }

    private void initWeatherInfoViews() {
        weatherInfoViews = new ArrayList<>();
        weatherInfoViews.add(txtCity);
        weatherInfoViews.add(txtTemperature);
        weatherInfoViews.add(txtStatus);
        weatherInfoViews.add(txtHumidity);
        weatherInfoViews.add(txtPressure);
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

                    txtCity.setText(city);
                    txtTemperature.setText(temperature + " C");
                    txtStatus.setText(status);
                    txtHumidity.setText(humidity + " %");
                    txtPressure.setText(pressure + " kPa");

                    // save weather values in preferences
                    saveUserInfoValue();
                }catch (NullPointerException e) {
                    // load values from preferences
                    loadWeatherInfoValue();

                    Log.d(TAG, e.getMessage());
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                hideProgress();

                // load values from preferences
                loadWeatherInfoValue();

                Log.d(TAG, t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
    private void loadWeatherInfoValue() {
        // TODO load values from preferences if connection is failed
        List<String> weatherData = dataManager.getPreferenceManager().loadWeatherData();
        for (int i = 0; i < weatherData.size(); i++) {
            weatherInfoViews.get(i).setText(weatherData.get(i).toString());
        }
    }
    private void saveUserInfoValue() {
        List<String> weatherData = new ArrayList<>();
        for (TextView weatherFieldView : weatherInfoViews) {
            weatherData.add(weatherFieldView.getText().toString());
        }
        dataManager.getPreferenceManager().saveWeatherData(weatherData);
    }
}
