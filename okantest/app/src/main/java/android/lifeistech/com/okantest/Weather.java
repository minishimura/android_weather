package android.lifeistech.com.okantest;

import com.squareup.moshi.Json;

public class Weather {

    @Json(name="name")
    private String weather;

    public Weather(String weather){
        super();
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }



}


