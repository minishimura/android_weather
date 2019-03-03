package android.lifeistech.com.okantest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements LocationListener {

    public String myId = "560565efd16ecd03e4fa586ec6aed1f4";
    public int cnt = 5;

    public float mlat;
    public float mlon;

    private WeatherAPI weatherAPI;
    private Retrofit retrofit;

    public boolean rain = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        weatherAPI = retrofit.create(WeatherAPI.class);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(provider, 10000, 0, this);

        if (rain) {
            notif();
        }
    }


    public void notif() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }

    public void getAPI(float lat,float lon) {

        Call<WeatherList> mCall = weatherAPI.requestListWeather(String.valueOf(lat), String.valueOf(lon), String.valueOf(cnt), myId);

        mCall.enqueue(new Callback<WeatherList>() {

            TextView textView = (TextView)findViewById(R.id.word);
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {

                List<WeatherList.ListA> weatherList = response.body().getList();


                if (weatherList != null) {
                    for (WeatherList.ListA l : weatherList) {
                        List<WeatherList.ListA.Weather> weather = l.getWeathers();

                        for(WeatherList.ListA.Weather w: weather){
                            Log.d("test",w.getMain());
                            if(Objects.equals(w.getMain(), "Rain")){
                                 rain = true;
                            }
                        }
                    }

                } else {
                    Log.d("API", "null");
                }
                if(rain){
                    textView.setText(R.string.rain);
                    notif();
                }else{
                    textView.setText(R.string.sun);
                }
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable t) {
                Log.d("API", "NG");
            }
        });

    }


    @Override
    public void onLocationChanged(Location location) {
        mlat = (float)location.getLatitude();
        mlon = (float)location.getLongitude();
        getAPI(mlat,mlon);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}






