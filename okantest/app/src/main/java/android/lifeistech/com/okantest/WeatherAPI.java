package android.lifeistech.com.okantest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("forecast?")
    Call<APIResponse> requestWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("cnt") String cnt,
            @Query("appid") String appid

    );
}

