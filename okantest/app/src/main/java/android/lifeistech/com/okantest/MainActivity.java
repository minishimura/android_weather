package android.lifeistech.com.okantest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    public String myId = "560565efd16ecd03e4fa586ec6aed1f4";
    public float lat = 39.1f;
    public float lon = 150.1f;
    public int cnt = 5;

    private WeatherAPI weatherAPI;
    private Retrofit retrofit;

    public boolean rain = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

  //      Moshi moshi = new Moshi.Builder().build();

//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.openweathermap.org/data/2.5/")
//                .addConverterFactory(MoshiConverterFactory.create(moshi))
//                .client(okHttpClient)
//                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        weatherAPI = retrofit.create(WeatherAPI.class);


        Call<List<Weather>> mCall = weatherAPI.getWeather(lat,lon,cnt,myId);

        mCall.enqueue(new Callback<List<Weather>>() {
            @Override
            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {

                List<Weather> weathers = response.body();

                if(weathers!=null){
                    Log.d("check","OK");
//                    for (Weather weather : weathers) {
//                      Log.d("weather", weather.getWeather());
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<Weather>> call, Throwable t) {
                Log.d("check","NG");

            }
        });

        if(rain){
            notif();
        }
    }


    public void notif(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

        }

    }




