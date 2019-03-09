package android.lifeistech.com.okantest;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
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


public class MainActivity extends AppCompatActivity {


    private Intent serviceIntent;
    private IMyService binder;

    private boolean b;

    SharedPreferences pref;



    private ServiceConnection connection = new ServiceConnection() {
        //サービスコネクション生成（サービス接続/非接続の時の処理）
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //IMyServiceオブジェクト取得
            binder = IMyService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

        @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            pref = getSharedPreferences("pref_weather",MODE_PRIVATE);

            serviceIntent = new Intent(this, NewService.class);
            startService(serviceIntent);
            b = getApplication().bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        }

        @Override
        public void onResume(){
            super.onResume();
            String a = pref.getString("weather","s");
            Log.d("test2",a);
        }


    public void notif() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,"test")
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }

}






