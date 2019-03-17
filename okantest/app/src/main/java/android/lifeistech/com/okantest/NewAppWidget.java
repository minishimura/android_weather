package android.lifeistech.com.okantest;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static SharedPreferences mpref;
    static String mWeather;
    static String[] weather8;
    static boolean isRain = false;
    static boolean f03;
    static boolean f36;
    static boolean f69;
    static boolean f912;
    static boolean f1215;
    static boolean f1518;
    static boolean f1821;
    static boolean f2124;

    static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    static SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
                    if(key.equals("weather")){
                        mWeather = mpref.getString("weather","s");
                        if(mWeather != null && mWeather.length() != 0){
                             weather8 =  mWeather.split(",");
                        }else{
                             weather8 = null;
                        }
                        if(weather8[0].equals("rain")){
                            isRain = true;
                            f03 = true;
                        }
                        if(weather8[1].equals("rain")){
                            isRain = true;
                            f36 = true;
                        }
                        if(weather8[2].equals("rain")){
                            isRain = true;
                            f69 = true;
                        }
                        if(weather8[3].equals("rain")){
                            isRain = true;
                            f912 = true;
                        }
                        if(weather8[4].equals("rain")){
                            isRain = true;
                            f1215 = true;
                        }
                        if(weather8[5].equals("rain")){
                            isRain = true;
                            f1518 = true;
                        }
                        if(weather8[6].equals("rain")){
                            isRain = true;
                            f1821 = true;
                        }
                        if(weather8[7].equals("rain")) {
                            isRain = true;
                            f2124 = true;
                        }

                    }
                }
            };

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        if(f03&&f36&&f69&&f912&&f1215&&f1518&&f1821&&f2124){
            views.setImageViewResource(R.id.widgetImage,R.drawable.widall);
        }else if(f03&&f36&&f69&&f912&&f1518){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid018);
        }else if(f1215&&f1518&&f1821&&f2124){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid120);
        }

        else if(f03&&f36&&f69&&f912){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid012);
        }else if(f03&&f36){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid06);
        }else if(f03){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid03);
        }else if(f1215){
            views.setImageViewResource(R.id.widgetImage,R.drawable.wid1215);
        }else{
            views.setImageViewResource(R.id.widgetImage,R.drawable.widsun2);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(ApplicationController.getInstance().getApplicationContext(), appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        mpref = context.getSharedPreferences("pref_weather" + mAppWidgetId,android.content.Context.MODE_WORLD_READABLE);
        mpref.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

