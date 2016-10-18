package com.example.android.lesson4alarm.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.android.lesson4alarm.Activity.AlarmActivity;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class AlarmService extends Service {

    Gson gson;
    ArrayList<Alarm> mAlarms;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    private final class WaitForAlarmInNewThread implements Runnable {

        Thread mThread;
        Calendar calendar;
        Alarm alarm;

        WaitForAlarmInNewThread(Alarm a) {
            mThread = new Thread(this, "WaitForAlarmInNewThread");
            alarm = a;
            mThread.start();
        }

        @Override
        public void run() {
            Log.v("log", "WaitForAlarmInNewThread start");

            while (!currentDay(alarm) && alarm.isRepeat) {
                synchronized (this) {
                    try {
                        wait(60000);
                        Log.v("log", "1 min");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!alarm.isRepeat) {
                calendar = getAlarmDateTime(alarm);
            } else {
                calendar = Calendar.getInstance();
            }


            calendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay);
            calendar.set(Calendar.MINUTE, alarm.minute);
            long alarmDateTimeInMillis = calendar.getTimeInMillis();

            while (System.currentTimeMillis() < alarmDateTimeInMillis) {
                synchronized (this) {
                    try {
                        wait(1000);
                        Log.v("log", "1 socond");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            startAlarm();

            Log.v("log", "startAlarm");
        }


    }

    private boolean currentDay(Alarm alarm) {
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

        sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
        mAlarms = new ArrayList<>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        getAlarmsFromShPrefAndConvertFromGson();

        for (Alarm alarm : mAlarms) {
            if (alarm.status) {
                new WaitForAlarmInNewThread(alarm);
            }
        }

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
        Log.v("log", "service onDestroy");
    }

    public void getAlarmsFromShPrefAndConvertFromGson() {
        String json = sharedPref.getString("Alarms", "empty");

        Type type = new TypeToken<ArrayList<Alarm>>() {
        }.getType();
        mAlarms = gson.fromJson(json, type);

    }

    public void startAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.v("log", "startAlarm done");

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


}
