package com.example.testmombetteryapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.sql.Time;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("oo","알람 테스트용 "+String.valueOf(System.currentTimeMillis()));

        Intent intentRepeat = new Intent(context,AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intentRepeat,0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,pendingIntent);



     //   BatteryAppWidget.updateAppWidget(context,AppWidgetManager.getInstance(context),R.layout.battery_app_widget);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        BatteryAppWidget app = new BatteryAppWidget();
        app.onUpdate(context,manager,manager.getAppWidgetIds(new ComponentName(context,BatteryAppWidget.class)));

    }
}
