package com.example.android.lesson4alarm.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.example.android.lesson4alarm.Activity.AlarmActivity;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.SingletonAlarm;
import java.util.Calendar;

public class AlarmService extends IntentService {

    SingletonAlarm sAlarms;

    public AlarmService() {
        super("AlarmService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("serv", "onHandleIntent");
        Calendar currentCalendar = Calendar.getInstance();

        // если список будильников не пустой работает проверка будильников
        while (!sAlarms.getAlarms().isEmpty()) {
            synchronized (this) {
                // проверяется кажидый из существующих будильников
                for (Alarm alarm : sAlarms.getAlarms()) {
                    Log.v("serv", "check");
                    if (alarm.status) {
                        Log.v("serv", "status ok");
                        if (isTimeForSingleAlarm(alarm)) {
                            alarm.status = false;
                            Log.v("serv", "start alarm single");
                            startAlarm();
                        } else if (isTimeForRepeatedAlarm(alarm)) {
                            alarm.DAY_OF_MONTH = currentCalendar.get(Calendar.DAY_OF_MONTH);
                            startAlarm();
                            Log.v("serv", "start alarm repeated");
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("serv", "onCreate");
        sAlarms = SingletonAlarm.initInstance(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void startAlarm() {
        sAlarms.saveAlarms();
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.v("serv", "startAlarm done");

    }

    // проверка условий для одиночного будильника
    private boolean isTimeForRepeatedAlarm(Alarm alarm) {
        Calendar currentCalendar = Calendar.getInstance();
        if (isCurrentDay(alarm) &&
                (currentCalendar.get(Calendar.HOUR_OF_DAY) == alarm.hourOfDay &&
                        currentCalendar.get(Calendar.MINUTE) == alarm.minute) &&
                currentCalendar.get(Calendar.DAY_OF_MONTH) != alarm.DAY_OF_MONTH) {
            return true;
        }
        return false;
    }

    // проверка условий для повторяемого по дням будильника
    private boolean isTimeForSingleAlarm(Alarm alarm) {
        Calendar currentCalendar = Calendar.getInstance();
        if (!alarm.isRepeat && (currentCalendar.get(Calendar.HOUR_OF_DAY) == alarm.hourOfDay &&
                currentCalendar.get(Calendar.MINUTE) == alarm.minute)
                && currentCalendar.get(Calendar.DAY_OF_MONTH) == alarm.DAY_OF_MONTH) {
            return true;
        } else {
            return false;
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

}
