package de.android.retrofit2elegionweather.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.android.retrofit2elegionweather.R;
import de.android.retrofit2elegionweather.data.managers.DataManager;
import de.android.retrofit2elegionweather.utils.ConstantManager;

public class DetailActivity extends BaseActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private List<TextView> weatherInfoViews;
    private DataManager dataManager;
    private String city;

    @BindView(R.id.txt_city) TextView txtCity;
    @BindView(R.id.txt_temperature) TextView txtTemperature;
    @BindView(R.id.txt_status) TextView txtStatus;
    @BindView(R.id.txt_humidity) TextView txtHumidity;
    @BindView(R.id.txt_pressure) TextView txtPressure;
    @BindView(R.id.txt_time) TextView txtTime;
    @BindView(R.id.detail_coordinator_container) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        dataManager = DataManager.getInstance();
        city = getIntent().getStringExtra(ConstantManager.CITY_NAME);

        initWeatherInfoViews();
        loadWeatherInfoValue();
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
}
