package com.example.android.lesson4alarm.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.lesson4alarm.Adapter.MyRecyclerViewAdapter;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.Services.AlarmService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyRecyclerViewAdapter.OnItemClick {


    ImageButton mAddAlarmButton;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    Gson gson;
    ArrayList<Alarm> mAlarms;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private static final String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        gson = new Gson();
        mAlarms = new ArrayList<>();
        sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAddAlarmButton = (ImageButton) findViewById(R.id.add_alarm_button);
        mAddAlarmButton.setOnClickListener(this);







    }

    @Override
    public void onResume() {
        super.onResume();

        getAlarmsFromShPrefAndConvertFromGson();

        showAlarms();

        startAlarmService();


        Log.d(TAG, "SignInActivity onResume() called");
    }


    public void getAlarmsFromShPrefAndConvertFromGson() {
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

    }

    public void convertAlarmsToGsonAndSaveToShPref() {
        String json = gson.toJson(mAlarms);
        editor.putString("Alarms", json);
        editor.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_alarm_button:
                Intent intent = new Intent(this, SetAlarmActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "SignInActivity onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "SignInActivity onPause() called");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SignInActivity onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SignInActivity onDestroy() called");
    }

    public void startAlarmService() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    public void stopAlarmService() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);
    }

    public void showAlarms() {
        mAdapter = new MyRecyclerViewAdapter(mAlarms, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onItemClickListenerRedactor(int position) {
        Intent intent = new Intent(this, SetAlarmActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onItemClickListenerStatus(int position) {
        mAlarms.get(position).status = !mAlarms.get(position).status;
        convertAlarmsToGsonAndSaveToShPref();

        showAlarms();
        Log.v("log", "onItemClickListenerStatus ok");
    }
}
