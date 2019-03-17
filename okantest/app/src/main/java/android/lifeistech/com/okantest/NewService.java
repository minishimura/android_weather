package android.lifeistech.com.okantest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewService extends Service implements LocationListener {

    private Handler handler = new Handler();
    private boolean running = false;
    private String message = "Message";

    public String myId = "560565efd16ecd03e4fa586ec6aed1f4";
    public int cnt = 8;

    public float lat;
    public float lon;

    private WeatherAPI weatherAPI;
    private Retrofit retrofit;

    SharedPreferences pref;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public String stringItem;


    @Override
    public void onCreate(){
        //サービス生成時
        //ニヤニヤしてます！（通報）
        super.onCreate();
                    retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

                    weatherAPI = retrofit.create(WeatherAPI.class);
        pref = getSharedPreferences("pref_weather"+ mAppWidgetId ,MODE_PRIVATE);

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

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(Intent intent, int flags, int startId) {
        //サービス開始時
        super.onStartCommand(intent, flags, startId);

        Thread thread = new Thread() {
            public void run() {
                running = true;
                while (running) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                           // toast(NewService.this, message);
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }
                }
            }
        };
        thread.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        running = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return IMyServiceBinder;
    }

    private static void toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    private final IMyService.Stub IMyServiceBinder = new IMyService.Stub() {
        @Override
        public void setMessage(String msg) throws RemoteException{
            message = msg;

        }
    };

    public void getAPI(float lat,float lon) {

        final SharedPreferences.Editor editor = pref.edit();
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.new_app_widget);

        Call<WeatherList> mCall = weatherAPI.requestListWeather(String.valueOf(lat), String.valueOf(lon), String.valueOf(cnt), myId);

        mCall.enqueue(new Callback<WeatherList>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {


                List<WeatherList.ListA> weatherList = response.body().getList();
                editor.clear().commit();
                if (weatherList != null) {
                    StringBuffer buffer = new StringBuffer();
                    for (WeatherList.ListA l : weatherList) {
                        List<WeatherList.ListA.Weather> weather = l.getWeathers();
                        for (WeatherList.ListA.Weather w : weather) {
                            buffer.append(w.getMain() + ",");
                        }
                    }String buf = buffer.toString();
                    stringItem = buf.substring(0, buf.length() - 1);
                    editor.putString("weather",stringItem).commit();
                    //Log.d("buffer",stringItem);

                    } else {
                    //Log.d("API", "null");
                }
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable t) {

            }
        });
        ComponentName thisWidget = new ComponentName(this, NewAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, remoteViews);
    }


            @Override
    public void onLocationChanged(Location location) {
        lat = (float)location.getLatitude();
        lon = (float)location.getLongitude();
        getAPI(lat,lon);
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


