package androidprojectsw.com.weather.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidprojectsw.com.weather.api.ApiConstants;
import androidprojectsw.com.weather.api.WeatherApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;


public class ApiManager {

    private static ApiManager sInstance;

    private WeatherApi mApiService;

    private ApiManager() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.WEATHER_SERVER_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApiService = retrofit.create(WeatherApi.class);
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }


    public WeatherApi getService() {
        return mApiService;
    }
}
