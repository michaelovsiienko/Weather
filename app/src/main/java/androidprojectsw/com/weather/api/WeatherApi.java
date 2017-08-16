package androidprojectsw.com.weather.api;


import androidprojectsw.com.weather.model.MainInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ubuntu on 8/15/17.
 */

public interface WeatherApi {

    @GET("data/2.5/weather")
    Observable<MainInfo> getWeatherInfo(@Query("q") String cityName, @Query("appid") String token, @Query("units") String units);

}
