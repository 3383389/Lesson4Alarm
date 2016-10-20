package com.example.android.lesson4alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Singleton for alarms
 */

public class SingletonAlarm {

    private static SingletonAlarm mInstance;

    private ArrayList<Alarm> mAlarms;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public static SingletonAlarm initInstance(Context context) {
        Log.w("MY_TAG", "MySingleton::getInstance()");
        if (mInstance == null) {
            mInstance = new SingletonAlarm(context);
        }
        return mInstance;
    }

    public static SingletonAlarm getInstatce() {
        return mInstance;
    }

    private SingletonAlarm(Context context) {
        gson = new Gson();
        mAlarms = new ArrayList<>();
        sharedPref = context.getSharedPreferences("mySettings", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        mAlarms = initAlarms();


    }

    public ArrayList<Alarm> getAlarms() {
        return mAlarms;
    }

    public ArrayList<Alarm> initAlarms() {

        String json = sharedPref.getString("Alarms", "empty");

        //if alarms empty adds new default alarm
        if (json.equals("empty")) {
            Alarm alarm = new Alarm();
            mAlarms.add(alarm);
            String s = gson.toJson(mAlarms);
            editor.putString("Alarms", s);
            editor.commit();
            json = sharedPref.getString("Alarms", "empty");
        }

        Type type = new TypeToken<ArrayList<Alarm>>() {
        }.getType();
        mAlarms = gson.fromJson(json, type);

        return mAlarms;
    }

    public void saveAlarms() {
        String json = gson.toJson(mAlarms);
        editor.putString("Alarms", json);
        editor.commit();
    }

    public void removeAlarm(int position) {
        if (position != -1) {
            mAlarms.remove(position);
        }
    }

    public void updateAlarm(int position, Alarm a) {
        if (position != -1)
            mAlarms.set(position, a);
    }

    public void addAlarm(Alarm a) {
        mAlarms.add(a);
    }

}
