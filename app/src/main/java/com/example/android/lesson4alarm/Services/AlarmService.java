package com.example.android.lesson4alarm.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.android.lesson4alarm.Activity.AlarmActivity;
import com.example.android.lesson4alarm.R;
import java.util.Calendar;




public class AlarmService extends Service {

    long alarmDateTimeInMillis;
    Intent mIntent;
    boolean mAlarmStatus;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    private final class WaitForAlarmInNewThread implements Runnable {

        Thread mThread;

        WaitForAlarmInNewThread() {
            mThread = new Thread(this, "WaitForAlarmInNewThread");
            mThread.start();
        }

        @Override
        public void run() {
            Log.v("log", "WaitForAlarmInNewThread start");


            alarmDateTimeInMillis = getAlarmDateTime().getTimeInMillis();

            while (System.currentTimeMillis() < alarmDateTimeInMillis && mAlarmStatus) {
                synchronized (this) {
                    try {
                        wait(1000);
                        Log.v("log", "1 socond");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (mAlarmStatus) {
                startAlarm();
                Log.v("log", "startAlarm");
            }
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        mIntent = intent;
        mAlarmStatus = sharedPref.getBoolean("alarm state", false);

        new WaitForAlarmInNewThread();

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
        mAlarmStatus = false;
        editor.putBoolean("alarm state", false);
        editor.commit();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        Log.v("log", "service onDestroy");
    }

    public void startAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        startActivity(intent);
        Log.v("log", "startAlarm done");

        stopSelf();
        Log.v("log", "stopSelf");
    }


    public Calendar getAlarmDateTime() {
        int hourOfDay = sharedPref.getInt("alarm hour", 0);
        int minute = sharedPref.getInt("alarm minute", 0);
        Calendar mSetCalendar = Calendar.getInstance();

        if (mSetCalendar.get(Calendar.HOUR_OF_DAY) > hourOfDay ||
                (mSetCalendar.get(Calendar.HOUR_OF_DAY) == hourOfDay && mSetCalendar.get(Calendar.MINUTE) >= minute)) {
            mSetCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mSetCalendar.set(Calendar.MINUTE, minute);
            mSetCalendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            mSetCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mSetCalendar.set(Calendar.MINUTE, minute);
        }
        return mSetCalendar;
    }


}
