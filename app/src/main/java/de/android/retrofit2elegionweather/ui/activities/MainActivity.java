package de.android.retrofit2elegionweather.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.data.managers.DataManager;
import de.android.retrofit2elegionweather.data.managers.PreferenceManager;
import de.android.retrofit2elegionweather.data.network.RestService;
import de.android.retrofit2elegionweather.data.network.weathermodelres.Model;
import de.android.retrofit2elegionweather.ui.adapters.WeatherAdapter;
import de.android.retrofit2elegionweather.utils.AppConfig;
import de.android.retrofit2elegionweather.utils.ConstantManager;
import de.android.retrofit2elegionweather.utils.NetworkStatusChecker;
import de.android.retrofit2elegionweather.utils.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DataManager dataManager;
    private String[] weatherInfo;
    private List<String> startCityList;
    private List<String> shortWeather;
    private WeatherAdapter adapter;
    private String city, temperature, status, pressure, humidity, time;

    @BindView(R.id.detail_coordinator_container) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.fab)FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dataManager = DataManager.getInstance();

        shortWeather = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new WeatherAdapter(shortWeather);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String cityName = shortWeather.get(position).split(" ")[0];
                startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra(ConstantManager.CITY_NAME, cityName));
            }
        }));

        initStartCityList();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(AddCityActivity.newIntent(
                        getApplicationContext()), ConstantManager.REQUEST_CODE);
            }
        });

//        if (NetworkStatusChecker.isNetworkAvailable(getApplicationContext())) {
//            getReport();
//        } else {
//            // TODO load values from preferences into fields
//            showSnackbar("Network connection failed");
//            getReportWithoutConnection();
//            loadWeatherInfoValue();
//        }

//        List<String> test = dataManager.getPreferenceManager().loadWeatherData("Berlin");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkStatusChecker.isNetworkAvailable(getApplicationContext())) {
            getReport();
        } else {
            // TODO load values from preferences into fields
            showSnackbar("Network connection failed");
            getReportWithoutConnection();
            loadWeatherInfoValue();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantManager.REQUEST_CODE && requestCode == RESULT_OK) {
            String newCity = data.getStringExtra(ConstantManager.RESULT);
            startCityList.add(newCity);
            Toast.makeText(MainActivity.this, startCityList.get(startCityList.size()), Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }

    private void getReportWithoutConnection() {
        for (int i = 0; i < startCityList.size(); i++) {
            weatherInfo = new String[PreferenceManager.WEATHER_FIELDS.length];
        }
    }

    private void initStartCityList() {
        startCityList = new ArrayList<>();
        startCityList.add("Berlin, de");
        startCityList.add("Braunschweig, de");
        startCityList.add("Koeln, de");
        startCityList.add("Muenchen, de");
        startCityList.add("Hamburg, de");
        startCityList.add("Bonn, de");
        startCityList.add("Kazan, ru");
    }

    private void getReport() {
        showProgress();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        for (int i = 0; i < startCityList.size(); i++) {
            RestService service = retrofit.create(RestService.class);
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
                        temperature = String.valueOf(Math.round(response.body().getMain().getTemp()));
                        status = response.body().getWeather().get(0).getDescription();
                        humidity = String.valueOf(response.body().getMain().getHumidity());
                        pressure = String.valueOf(response.body().getMain().getPressure());

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm z", Locale.GERMANY);
                        TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
                        dateFormat.setTimeZone(tz);
                        time = dateFormat.format(System.currentTimeMillis());

                        weatherInfo = new String[] {
                                city,
                                temperature,
                                status,
                                humidity,
                                pressure,
                                time
                        };

                        Log.d("LOG", city);
                        Log.d("LOG", temperature);
                        shortWeather.add(city + " " + temperature + " C");
                        Log.d("LOG", "" + shortWeather.size());

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
        List<String> weatherData = dataManager.getPreferenceManager().loadWeatherData(city);
        for (int i = 0; i < weatherData.size(); i++) {
            weatherInfo[i] = weatherData.get(i);
        }
    }
    private void saveUserInfoValue() {
        List<String> weatherData = new ArrayList<>();
        for (int i = 0; i < weatherInfo.length; i++) {
            weatherData.add(weatherInfo[i]);
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
