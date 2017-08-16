package androidprojectsw.com.weather.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import androidprojectsw.com.weather.Constants;
import androidprojectsw.com.weather.R;
import androidprojectsw.com.weather.Utils;
import androidprojectsw.com.weather.api.ApiConstants;
import androidprojectsw.com.weather.contract.MainContract;
import androidprojectsw.com.weather.model.MainInfo;
import androidprojectsw.com.weather.presenter.MainPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private String mCityName;
    private BroadcastReceiver mReceiver;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressBar)
    ProgressBar mProgress;
    @BindView(R.id.main_layout)
    ConstraintLayout mLayoutMain;
    @BindView(R.id.tv_city_name)
    TextView mTextViewCityName;
    @BindView(R.id.tv_current_temp)
    TextView mTextViewCurrentTemp;
    @BindView(R.id.tv_minmax_temp)
    TextView mTextViewMinMaxTemp;
    @BindView(R.id.tv_weather_description)
    TextView mTextViewWeatherDesc;
    @BindView(R.id.tv_last_update)
    TextView mTextViewLastUpdate;

    @BindView(R.id.iv_city_icon)
    ImageView mImageViewCityIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPresenter = new MainPresenter(this);

        if (!mPresenter.checkInternetStatus()) {
            Toast.makeText(this, getString(R.string.error_network_state), Toast.LENGTH_LONG).show();
            finish();
        }

        mCityName = mPresenter.getChosenCity();

        mPresenter.fetchWeatherInfo(mCityName);

        scheduleAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPresenter.checkInternetStatus()) {
                    mPresenter.fetchWeatherInfo(mCityName);
                    mTextViewLastUpdate.setText(String.format(getString(R.string.last_update), Utils.getFormattedTime()));
                }
            }
        };

        this.registerReceiver(mReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading(boolean isShow) {
        mProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mLayoutMain.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showWeatherInfo(MainInfo info) {
        mTextViewCityName.setText(mCityName);
        mTextViewCurrentTemp.setText(String.format(getResources().getString(R.string.current_temp), info.getMain().getTemp()));
        mTextViewWeatherDesc.setText(String.valueOf(info.getWeather().get(0).getDescription()));
        mTextViewMinMaxTemp.setText(String.format(getResources().getString(R.string.min_max_temp), info.getMain().getTempMax(), info.getMain().getTempMin()));
        Picasso
                .with(getContext())
                .load(String.format(getResources().getString(R.string.weather_image_url),
                        ApiConstants.IMAGE_SERVER_URL, info.getWeather().get(0).getIcon()))
                .into(mImageViewCityIcon);
        mTextViewLastUpdate.setText(String.format(getString(R.string.last_update), Utils.getFormattedTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), Constants.SETTING_REQUEST_CODE);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.SETTING_REQUEST_CODE) {
            if (data.getStringExtra(Constants.CITY_NAME) != null) {
                mCityName = data.getStringExtra(Constants.CITY_NAME);
                mPresenter.editChosenCity(mCityName);
                mPresenter.fetchWeatherInfo(mCityName);
            }
        }
    }

    private void scheduleAlarm() {

        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(Constants.BROADCAST_ACTION), 0);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR, pIntent);
        }
    }
}
