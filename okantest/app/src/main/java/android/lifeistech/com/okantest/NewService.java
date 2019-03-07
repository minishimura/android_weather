package android.lifeistech.com.okantest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class NewService extends Service {

    private Handler handler = new Handler();
    private boolean running = false;
    private String message = "Message";

    @Override
    public void onCreate(){
        //サービス生成時
        super.onCreate();
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
}


