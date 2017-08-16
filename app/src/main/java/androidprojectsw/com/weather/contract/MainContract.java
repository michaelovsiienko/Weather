package androidprojectsw.com.weather.contract;


import androidprojectsw.com.weather.model.MainInfo;

public interface MainContract {
    interface View extends BaseContract.View {

        void showWeatherInfo(MainInfo info);

    }

    interface Presenter extends BaseContract.Presenter<MainContract.View> {

        void fetchWeatherInfo(String city);

        String getChosenCity();

        void editChosenCity(String cityName);

        boolean checkInternetStatus();
    }
}
