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
    public int cnt = 5;

    public float lat;
    public float lon;

    private WeatherAPI weatherAPI;
    private Retrofit retrofit;

    public boolean rain = false;

    SharedPreferences pref;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    public void onCreate(){
        //サービス生成時
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

        showNotification(this, "a", "a");
        Thread thread = new Thread() {
            public void run() {
                running = true;
                while (running) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            toast(NewService.this, message);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, String title, String text){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("android.lifeistech.com.okantest","android.lifeistech.com.okantest.MainActivity"));
        intent.removeCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.setContentIntent(PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT));

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(1);
        nm.notify(1,builder.build());
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
                if (weatherList != null) {
                    for (WeatherList.ListA l : weatherList) {
                        List<WeatherList.ListA.Weather> weather = l.getWeathers();

                        editor.clear().commit();
                        for (WeatherList.ListA.Weather w : weather) {
                            //Log.d("test", w.getMain());
                            editor.putString("weather",w.getMain()).commit();
                            if (Objects.equals(w.getMain(), "Rain")) {
                                rain = true;
                            }
                        }
                    }
                } else {
                    Log.d("API", "null");
                }
                if (rain) {
                    remoteViews.setTextViewText(R.id.testText,"rain");
                    Log.d("testText","rain");

                } else {
                    remoteViews.setTextViewText(R.id.testText, "notRain");
                    Log.d("testText","notrain");

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
        Log.d("test",String.valueOf(lat));
        Log.d("test",String.valueOf(lon));
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


