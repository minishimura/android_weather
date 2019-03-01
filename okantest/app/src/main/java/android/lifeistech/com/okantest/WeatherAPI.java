package android.lifeistech.com.okantest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("forecast")
    Call<List<Weather>> getWeather(
            @Query("lat") float lat,
            @Query("lon") float lon,
            @Query("cnt") int cnt,
            @Query("appid") String appid
    );
}

