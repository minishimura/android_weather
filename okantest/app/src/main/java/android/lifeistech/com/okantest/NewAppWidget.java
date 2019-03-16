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
    static String[] weather;
    static boolean isRain = false;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    static SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
                    if(key.equals("weather")){
                        mWeather = mpref.getString("weather","s");
                        if(mWeather != null && mWeather.length() != 0){
                             weather =  mWeather.split(",");
                        }else{
                             weather = null;
                        }
                        Log.d("array1",String.valueOf(weather[0]));
                        if(weather[0].equals("rain")){
                            isRain = true;

                        }
                        if(weather[1].equals("rain")){
                            isRain = true;

                        }
                        if(weather[2].equals("rain")){
                            isRain = true;

                        }
                        if(weather[3].equals("rain")){
                            isRain = true;

                        }
                        if(weather[4].equals("rain")){
                            isRain = true;

                        }

                        Log.d("array2",String.valueOf(weather[1]));
                        Log.d("array2",String.valueOf(weather[2]));
                        Log.d("array2",String.valueOf(weather[3]));
                        Log.d("array2",String.valueOf(weather[4]));




                    }
                }
            };

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //mpref.registerOnSharedPreferenceChangeListener(listener);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.testText,"rain");


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

