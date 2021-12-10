package com.example.testmombetteryapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.widget.DigitalClock;
import android.widget.RemoteViews;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class BatteryAppWidget extends AppWidgetProvider {
//    Timer TotalTimer;
    static float NowbatteyPercent;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
   //     Log.d("oo","updatewidget");
        //여기가 매초마다 작동

       // CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
      //  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_app_widget);
       // views.setTextViewText(R.id.appwidget_text, String.valueOf(System.currentTimeMillis()));
      //  views.setTextViewText(R.id.button, "메롱");// ㅇㅎ?


        //Log.d("oo","업뎃");

       // NowbatteyPercent = 50;
        IntentFilter intentFilter = new IntentFilter( Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null,intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        NowbatteyPercent =  (level *100) /(float)scale;
//        String text = String.valueOf((int)NowbatteyPercent)+"%";
//        views.setTextViewText(R.id.appwidget_text, text);
      //  views.setTextViewText(R.id.appwidget_text, String.valueOf( System.currentTimeMillis()));

        SharedPreferences sharedPreferences = context.getSharedPreferences("test",Context.MODE_PRIVATE);

        Boolean percentBool = sharedPreferences.getBoolean("PercentBool",true);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_app_widget);
        if(percentBool){
            //String text = ;
            views.setTextViewText(R.id.appwidget_text, String.valueOf((int)NowbatteyPercent)+"%");
        }else {
           // String text = String.valueOf((int)NowbatteyPercent);
            views.setTextViewText(R.id.appwidget_text, String.valueOf((int)NowbatteyPercent));
        }

        Boolean Ok = sharedPreferences.getBoolean("TitelTextColor",true);
        if(Ok){
            views.setTextColor(R.id.appwidget_text, Color.WHITE);
        }else {
            views.setTextColor(R.id.appwidget_text, Color.BLACK);
        }
        int Testsize = sharedPreferences.getInt("TitelTextSize",36);

        views.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_DIP,Testsize);

        if (NowbatteyPercent>=75) {
            views.setImageViewResource(R.id.imageView, R.drawable.spring);
        }else if (NowbatteyPercent>=50){
            views.setImageViewResource(R.id.imageView, R.drawable.summer);
        } else if (NowbatteyPercent>=25) {
            views.setImageViewResource(R.id.imageView, R.drawable.fall);
        }else {
            views.setImageViewResource(R.id.imageView, R.drawable.winter);
        }


        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,alarmIntent);//이건 작동함


        // Instruct the widget manager to update the widget
//        if(MainActivity.WhiteColor){
//            views.setTextColor(R.id.appwidget_text, context.getResources().getColor(R.color.white));
//        }else {
//            views.setTextColor(R.id.appwidget_text, context.getResources().getColor(R.color.black));
//        }
        appWidgetManager.updateAppWidget(appWidgetId, views);



    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
        //try1
    //    Log.d("oo","onUpdate");
        SetFirstSetting(context);
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);

         //   Log.d("oo","14");
            Intent intent = new Intent(context,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.battery_app_widget);
            views.setOnClickPendingIntent(R.id.totalView,pendingIntent);

            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId,views);
            //버튼 누르면 앱이 켜짐


//타이머로 하는 법
//            if(TotalTimer ==null){
//                TotalTimer = new Timer();
//            }
//            TotalTimer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    //Log.d("oo","??");
//                    //updateAppWidget(context, appWidgetManager, appWidgetId);
//                    IntentFilter intentFilter = new IntentFilter( Intent.ACTION_BATTERY_CHANGED);
//                    Intent batteryStatus = context.registerReceiver(null,intentFilter);
//
//                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
//                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
//                    NowbatteyPercent =  (level *100) /(float)scale;
//                    //Log.d("oo",String.valueOf(MainActivity.WhiteColor));
//                    if((int)NowbatteyPercent != (int)BeforebatteyPercent){
//                        updateAppWidget(context, appWidgetManager, appWidgetId);
//                        BeforebatteyPercent = NowbatteyPercent;
//                    }
//
//                }
//            },0,3000);


        }




    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        SetFirstSetting(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

//        if(TotalTimer !=null) {
//            TotalTimer.cancel();
//        }
//        TotalTimer = new Timer();
        //Log.d("oo","BattonEnable");

        SetFirstSetting(context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
//        if(TotalTimer !=null) {
//            TotalTimer.cancel();
//        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
//        if(TotalTimer !=null) {
//            TotalTimer.cancel();
//        }


        super.onDeleted(context, appWidgetIds);
    }

    void SetFirstSetting(Context context){
      //  Log.d("oo","It's me!");

        IntentFilter intentFilter = new IntentFilter( Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null,intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        NowbatteyPercent = (int) ((level *100) /(float)scale);


       // views.setTextViewText(R.id.appwidget_text, String.valueOf( System.currentTimeMillis()));


        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,alarmIntent);//이건 작동함

        SharedPreferences sharedPreferences = context.getSharedPreferences("test",Context.MODE_PRIVATE);


        Boolean percentBool = sharedPreferences.getBoolean("PercentBool",true);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_app_widget);
        if(percentBool){
            views.setTextViewText(R.id.appwidget_text, String.valueOf((int)NowbatteyPercent)+"%");
        }else {
            views.setTextViewText(R.id.appwidget_text, String.valueOf((int)NowbatteyPercent));
        }


        Boolean Ok = sharedPreferences.getBoolean("TitelTextColor",true);
        if(Ok){
            views.setTextColor(R.id.appwidget_text, Color.WHITE);
        }else {
            views.setTextColor(R.id.appwidget_text, Color.BLACK);
        }
        int Testsize = sharedPreferences.getInt("TitelTextSize",36);

        views.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_DIP,Testsize);

        if (NowbatteyPercent>=75) {
            views.setImageViewResource(R.id.imageView, R.drawable.spring);
        }else if (NowbatteyPercent>=50){
            views.setImageViewResource(R.id.imageView, R.drawable.summer);
        } else if (NowbatteyPercent>=25) {
            views.setImageViewResource(R.id.imageView, R.drawable.fall);
        }else {
            views.setImageViewResource(R.id.imageView, R.drawable.winter);
        }

    }
}