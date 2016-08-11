package de.android.retrofit2elegionweather.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.android.retrofit2elegionweather.BuildConfig;
import de.android.retrofit2elegionweather.POJO.Model;
import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.RestInterface;
import de.android.retrofit2elegionweather.data.managers.DataManager;
import de.android.retrofit2elegionweather.utils.ConstantManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DataManager dataManager;
    private List<TextView> weatherInfoViews;
    private List<String> startCityList;
    private String city;

    @BindView(R.id.txt_city) TextView txtCity;
    @BindView(R.id.txt_temperature) TextView txtTemperature;
    @BindView(R.id.txt_status) TextView txtStatus;
    @BindView(R.id.txt_humidity) TextView txtHumidity;
    @BindView(R.id.txt_pressure) TextView txtPressure;
    @BindView(R.id.txt_time) TextView txtTime;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startCityListInit();

        dataManager = DataManager.getInstance();

        initWeatherInfoViews();

        getReport();

//        List<String> test = dataManager.getPreferenceManager().loadWeatherData();
    }

    private void startCityListInit() {
        startCityList = new ArrayList<>();
        startCityList.add("Berlin, de");
        startCityList.add("Braunschweig, de");
        startCityList.add("Koeln, de");
        startCityList.add("Muenchen, de");
        startCityList.add("Hamburg, de");
    }

    private void initWeatherInfoViews() {
        weatherInfoViews = new ArrayList<>();
        weatherInfoViews.add(txtCity);
        weatherInfoViews.add(txtTemperature);
        weatherInfoViews.add(txtStatus);
        weatherInfoViews.add(txtHumidity);
        weatherInfoViews.add(txtPressure);
        weatherInfoViews.add(txtTime);
    }

    private void getReport() {
        showProgress();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        for (int i = 0; i < startCityList.size(); i++) {
            RestInterface service = retrofit.create(RestInterface.class);
            Call<Model> call = service.getWeatherReport(
                    startCityList.get(i),
                    ConstantManager.JSON_MODE,
                    ConstantManager.METRIC_MODE,
                    ConstantManager.LANGUAGE_MODE,
                    BuildConfig.OPEN_WEATHER_MAP_API_KEY);

            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    hideProgress();
                    try {
                        city = response.body().getName();
                        String temperature = String.valueOf(Math.round(response.body().getMain().getTemp()));
                        String status = response.body().getWeather().get(0).getDescription();
                        String humidity = String.valueOf(response.body().getMain().getHumidity());
                        String pressure = String.valueOf(response.body().getMain().getPressure());

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm z", Locale.GERMANY);
                        TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
                        dateFormat.setTimeZone(tz);
                        String time = dateFormat.format(System.currentTimeMillis());

                        txtCity.setText(city);
                        Log.d("LOG", city);
                        txtTemperature.setText(temperature);
                        Log.d("LOG", temperature);
                        txtStatus.setText(status);
                        txtHumidity.setText(humidity);
                        txtPressure.setText(pressure);
                        txtTime.setText(time);

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

    }
    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
    private void loadWeatherInfoValue() {
        // TODO load values from preferences if connection is failed
        List<String> weatherData = dataManager.getPreferenceManager().loadWeatherData(city);
        for (int i = 0; i < weatherData.size(); i++) {
            weatherInfoViews.get(i).setText(weatherData.get(i).toString());
        }
    }
    private void saveUserInfoValue() {
        List<String> weatherData = new ArrayList<>();
        for (TextView weatherFieldView : weatherInfoViews) {
            weatherData.add(weatherFieldView.getText().toString());
        }
        dataManager.getPreferenceManager().saveWeatherData(weatherData, city);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.DIALOG:
                String[] selectItems = {
                        getString(R.string.add_new_city),
                        getString(R.string.delete_city_from_list),
                        getString(R.string.cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.select_action)
                        .setItems(selectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        showSnackbar("Add");
                                        break;
                                    case 1:
                                        showSnackbar("Delete");
                                        break;
                                    case 2:
                                        showSnackbar("Cancel");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
                return builder.create();
            default:
                return null;
        }
    }
}
