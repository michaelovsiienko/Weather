package androidprojectsw.com.weather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainInfo {

    @SerializedName("weather")
    private List<Weather> weather = null;

    @SerializedName("main")
    private TempInfo main;



    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    public TempInfo getMain() {
        return main;
    }

    public void setMain(TempInfo main) {
        this.main = main;
    }
}
