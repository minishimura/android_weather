package android.lifeistech.com.okantest;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class Weather {

    //@Json(name="name")
    @SerializedName("dt_txt")
    private String dt_txt;

//    public Weather(String weather){
//        super();
//        this.weather = weather;
//    }

    public String getWeather() {
        return dt_txt;
    }



}


