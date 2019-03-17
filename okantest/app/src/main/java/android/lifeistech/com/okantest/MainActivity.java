package android.lifeistech.com.okantest;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

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
    private boolean rain;


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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            TextView textView = (TextView)findViewById(R.id.word) ;
            rain = NewAppWidget.isRain;
            //rain=true;
            if(rain){
                textView.setText(R.string.rain);
                showNotification(this, "From　母", "傘忘れたらあかんで〜");
            }else{
                textView.setText(R.string.sun);
            }

            NewAppWidget.isRain = false;

            serviceIntent = new Intent(this, NewService.class);
            startService(serviceIntent);
            b = getApplication().bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        }

        @Override
        public void onResume(){
            super.onResume();
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
}






