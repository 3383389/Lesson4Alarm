package com.example.android.lesson4alarm.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.android.lesson4alarm.Activity.AlarmActivity;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.SingletonAlarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class AlarmService extends Service {

    SingletonAlarm sAlarms;

    WaitForAlarmInNewThread threads;


    private final class WaitForAlarmInNewThread extends Thread {

//        Calendar calendar;
//        Alarm alarm;
//
//        WaitForAlarmInNewThread(Alarm a) {
//            alarm = a;
//        }

        @Override
        public void run() {
            Log.v("serv", "WaitForAlarmInNewThread start");



            while (true) {
                synchronized (this) {
                    try {
                        wait(1000);
                        Log.v("serv", "1 s");
                        stopSelf();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
//
//            if (!alarm.isRepeat) {
//                calendar = getAlarmDateTime(alarm);
//            } else {
//                calendar = Calendar.getInstance();
//            }
//
//
//            calendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay);
//            calendar.set(Calendar.MINUTE, alarm.minute);
//            long alarmDateTimeInMillis = calendar.getTimeInMillis();
//
//            while (System.currentTimeMillis() < alarmDateTimeInMillis) {
//                synchronized (this) {
//                    try {
//                        wait(1000);
//                        Log.v("log", "1 socond");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//
//            startAlarm();
//
//            Log.v("log", "startAlarm");
        }



    }

    private boolean isCurrentDay(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        for (int i : alarm.DAY_OF_WEEK) {
            if (i == currentDay) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("serv", "onCreate");
        sAlarms = SingletonAlarm.initInstance(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Log.v("serv", "onStartCommand");

        int counter = 0;

   //     threads = new WaitForAlarmInNewThread[sAlarms.getAlarms().size()];
//


        threads = new WaitForAlarmInNewThread();
        threads.start();
//        for (Alarm alarm : sAlarms.getAlarms()) {
//            if (alarm.status) {
//                threads[counter] = new WaitForAlarmInNewThread(alarm);
//                counter++;
//            }
//        }

        Log.v("log", "OnStartCommand done");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        Log.v("serv", "service onDestroy");
    }


    public void startAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.v("serv", "startAlarm done");

    }


    public Calendar getAlarmDateTime(Alarm alarm) {

        Calendar mSetCalendar = Calendar.getInstance();

        if (mSetCalendar.get(Calendar.HOUR_OF_DAY) > alarm.hourOfDay ||
                (mSetCalendar.get(Calendar.HOUR_OF_DAY) == alarm.hourOfDay && mSetCalendar.get(Calendar.MINUTE) >= alarm.minute)) {
            mSetCalendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay);
            mSetCalendar.set(Calendar.MINUTE, alarm.minute);
            mSetCalendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            mSetCalendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay);
            mSetCalendar.set(Calendar.MINUTE, alarm.minute);
        }
        return mSetCalendar;
    }
//
//    public void stopThreds() {
//        for (WaitForAlarmInNewThread thred : threds) {
//            if (thred != null) {
//                if (thred.alive())
//                    thred.stop();
//            }
//        }
//
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v("serv", "onConfigurationChanged");
    }


}
