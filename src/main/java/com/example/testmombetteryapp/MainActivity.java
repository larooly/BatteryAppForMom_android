package com.example.testmombetteryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView showNow;
    Button textColor;
    Button smallBtn;
    Button midBtn;
    Button bigBtn;
    public static  boolean WhiteColor = true;
    public static final String ColorKey = "WHITE_COLOR_YES";

    //private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private AlarmManager alarmManager;
    private  PendingIntent alarmIntent;

    Switch percentDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.SplashTeme);

        showNow = findViewById(R.id.ShowNowFont);
        textColor = findViewById(R.id.SwitchColor);
        smallBtn = findViewById(R.id.buttonSmall);
        midBtn = findViewById(R.id.buttonMid);
        bigBtn = findViewById(R.id.buttonBig);

        percentDisplay = findViewById(R.id.percentSwitch);

//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){//android 6 부터
//            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            if(powerManager.isIgnoringBatteryOptimizations(getPackageName())==false){
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//                startActivity(intent);
//
//            }
//
//        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){//android 6 부터
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if(!powerManager.isIgnoringBatteryOptimizations(getPackageName())){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:"+getPackageName()));

                startActivity(intent);

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS},0);
            }
        }




            testAlarmManager();

        if(getTextColorBool()){
            textColor.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(),R.color.white));
            textColor.setText("흰색");
            textColor.setTextColor(getResources().getColor(R.color.black));
            WhiteColor = true;
        }else {
            textColor.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(),R.color.black));
            textColor.setText("검정");
            textColor.setTextColor(getResources().getColor(R.color.white));
            WhiteColor = false;
        }

        if(getPercentBool()){
            percentDisplay.setChecked(true);
        }else {
            percentDisplay.setChecked(false);
        }

        percentDisplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setPercentBool(true);

                }else {
                    setPercentBool(false);
                }
            }
        });
        switch (getTextFontSize()){
            case 36:
                showNow.setText("현재 사이즈 : 작게");
                break;
            case 48:
                showNow.setText("현재 사이즈 : 보통");
                break;
            case 60:
                showNow.setText("현재 사이즈 : 크게");
                break;
        }

        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WhiteColor){//white->black
                    textColor.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(),R.color.black));
                    textColor.setText("검정");
                    textColor.setTextColor(getResources().getColor(R.color.white));
                    WhiteColor = false;
                    setTextColorBool(WhiteColor);
                  //  confirmConfiguration(textColor);
                }else {//black ->white
                    textColor.setBackgroundTintList(ContextCompat.getColorStateList(getBaseContext(),R.color.white));
                    textColor.setText("흰색");
                    textColor.setTextColor(getResources().getColor(R.color.black));

                    WhiteColor = true;
//                    confirmConfiguration(textColor);
                    setTextColorBool(WhiteColor);
                }
            }
        });

        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextFontSize(36);
                showNow.setText("현재 사이즈 : 작게");
            }
        });
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextFontSize(48);
                showNow.setText("현재 사이즈 : 보통");
            }
        });
        bigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextFontSize(60);
                showNow.setText("현재 사이즈 : 크게");
            }
        });

    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d("ooo","z: "+String.valueOf(permissions.length));
//        switch (requestCode){
//            case 0:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Log.d("ooo","ㅇㅇ");
//                }else {
//                    Log.d("ooo","ㄴㄴ");
//                }
//                return;
//            default:
//                Log.d("ooo","kk");
//                return;
//        }
//    }

    public void setTextColorBool(Boolean bool){
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("TitelTextColor",bool);
        editor.commit();
    }
    public Boolean getTextColorBool(){
        SharedPreferences sharedPreferences = getSharedPreferences("test",Context.MODE_PRIVATE);
        Boolean Ok = sharedPreferences.getBoolean("TitelTextColor",true);
        return Ok;
    }
    public void setTextFontSize(int num){
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("TitelTextSize",num);
        editor.commit();
    }
    public int getTextFontSize(){
        SharedPreferences sharedPreferences = getSharedPreferences("test",Context.MODE_PRIVATE);
        int Ok = sharedPreferences.getInt("TitelTextSize",0);
        return Ok;
    }

    public void setPercentBool(Boolean bool){
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("PercentBool",bool);
        editor.commit();
    }
    public Boolean getPercentBool(){
        SharedPreferences sharedPreferences = getSharedPreferences("test",Context.MODE_PRIVATE);
        Boolean Ok = sharedPreferences.getBoolean("PercentBool",true);
        return Ok;
    }
    public void testAlarmManager(){

//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY,14);
//        calendar.set(Calendar.MINUTE,57);
//        calendar.set(Calendar.SECOND,0);
////
       // Log.d("oo","MainActivity");

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this,1,intent,0);

//        if(calendar.before(Calendar.getInstance())){
//           // calendar.add(Calendar.DATE,1);//이미지난 시간일 경우 표시
//        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,alarmIntent);//이건 작동함

        //자 이제 이걸 반복으로 바꾸면 된다
      //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtimeNanos(),100,alarmIntent);
      //  alarmManager.setInexactRepeating(AlarmManager.RTC,System.currentTimeMillis(),1000*60,alarmIntent);




//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,calendar.getTimeInMillis(),1000*5,alarmIntent);


//        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
//        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),1000,alarmIntent);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+1000,alarmIntent);
    }

//    public void confirmConfiguration(View v){
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
//
//        boolean white = WhiteColor;
//        RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.battery_app_widget);
//        if(white){
//            views.setTextColor(R.id.appwidget_text,getResources().getColor(R.color.white));
//        }else {
//            views.setTextColor(R.id.appwidget_text,getResources().getColor(R.color.black));
//        }
//        appWidgetManager.updateAppWidget(appWidgetId,views);
//
//
//    }
}

// tv = findViewById(R.id.tv);

//        Intent configIntent = getIntent();
//        Bundle extras = configIntent.getExtras();
//        if(extras != null){
//            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
//        }
//        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
//            finish();
//
//        }


//        IntentFilter intentFilter = new IntentFilter( Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = this.registerReceiver(null,intentFilter);
//
//        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
//        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
       // tv.setText(String.valueOf((level *100) /(float)scale));//현재 베터리 퍼센트