package com.example.android.lesson4alarm.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.lesson4alarm.Activity.AlarmActivity;
import com.example.android.lesson4alarm.Activity.SetAlarmActivity;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.EventBus.MessageEvent;
import com.example.android.lesson4alarm.SingletonAlarm;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends Service {

    SingletonAlarm sAlarms;
    Timer timer;

    public class CheckAlarms extends TimerTask {

        Calendar currentCalendar;

        @Override
        public void run() {
            Log.v("serv", "CheckAlarms Run");

            currentCalendar = Calendar.getInstance();

            // если список будильников не пустой работает проверка будильников
            if (!sAlarms.getAlarms().isEmpty()) {
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
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("serv", "onCreate");
        EventBus.getDefault().register(this);
        sAlarms = SingletonAlarm.initInstance(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.v("serv", "onStartCommand ok");

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new CheckAlarms(), 1000, 20000);

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
        return isCurrentDay(alarm) &&
                (currentCalendar.get(Calendar.HOUR_OF_DAY) == alarm.hourOfDay &&
                        currentCalendar.get(Calendar.MINUTE) == alarm.minute) &&
                currentCalendar.get(Calendar.DAY_OF_MONTH) != alarm.DAY_OF_MONTH;
    }

    // проверка условий для повторяемого по дням будильника
    private boolean isTimeForSingleAlarm(Alarm alarm) {
        Calendar currentCalendar = Calendar.getInstance();
        return !alarm.isRepeat && (currentCalendar.get(Calendar.HOUR_OF_DAY) == alarm.hourOfDay &&
                currentCalendar.get(Calendar.MINUTE) == alarm.minute)
                && currentCalendar.get(Calendar.DAY_OF_MONTH) == alarm.DAY_OF_MONTH;
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

    public void startRedactorActivity(int position) {
        Intent intent = new Intent(this, SetAlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        Log.v("serv", "event ok");
        switch (event.message) {
            case DELETE_ALARM:
                if (event.position != null)
                    sAlarms.removeAlarm(event.position);
                sAlarms.saveAlarms();
                Log.v("serv", "del ok");
                break;
            case UPDATE_ALARM:
                if (event.position != null && event.alarm != null)
                    sAlarms.updateAlarm(event.position, event.alarm);
                sAlarms.saveAlarms();
                Log.v("serv", "update ok");
                break;
            case ADD_ALARM:
                if (event.alarm != null)
                    sAlarms.addAlarm(event.alarm);
                sAlarms.saveAlarms();
                Log.v("serv", "add ok");
                break;
            case SAVE_ALARM:
                sAlarms.saveAlarms();
                Log.v("serv", "save ok");
                break;
            case REDACTOR_ALARM:
                if (event.position != null)
                    startRedactorActivity(event.position);
                Log.v("serv", "save ok");
                break;
            case CHANGE_STATUS:
                if (event.position != null)
                    sAlarms.changeStatus(event.position);
                sAlarms.saveAlarms();
                break;
        }
    }

}
