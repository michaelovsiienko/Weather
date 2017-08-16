package androidprojectsw.com.weather.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidprojectsw.com.weather.Constants;
import androidprojectsw.com.weather.R;
import androidprojectsw.com.weather.contract.MainContract;
import androidprojectsw.com.weather.manager.ApiManager;
import androidprojectsw.com.weather.model.MainInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainPresenter implements MainContract.Presenter {

    private SharedPreferences mPreferences;
    private MainContract.View mView;
    private Observable<MainInfo> mMainInfoObservable;

    public MainPresenter(MainContract.View view) {
        attachView(view);
    }

    @Override
    public void attachView(MainContract.View view) {
        mView = view;
        mPreferences = mView.getContext().getSharedPreferences(Constants.USER_SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void fetchWeatherInfo(String city) {
        mMainInfoObservable = ApiManager
                .getInstance()
                .getService()
                .getWeatherInfo(city, Constants.USER_TOKEN, Constants.UNITS);
        mView.showLoading(true);
        mMainInfoObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MainInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MainInfo info) {
                        mView.showWeatherInfo(info);
                    }
                });
    }

    @Override
    public String getChosenCity() {
        return mPreferences.getString(Constants.CITY_NAME, mView.getContext().getString(R.string.default_city));
    }

    @Override
    public void editChosenCity(String cityName) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.CITY_NAME, cityName);
        editor.apply();
    }

    @Override
    public boolean checkInternetStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) mView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
