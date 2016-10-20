package com.example.android.lesson4alarm;

import android.app.Application;
import android.content.Intent;

import com.example.android.lesson4alarm.Services.AlarmService;

/**
 * Created by user on 19.10.2016.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SingletonAlarm.initInstance(this);

        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }




}
