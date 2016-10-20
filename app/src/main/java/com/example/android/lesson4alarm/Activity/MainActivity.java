package com.example.android.lesson4alarm.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.example.android.lesson4alarm.Adapter.RecyclerViewAdapter;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.Services.AlarmService;
import com.example.android.lesson4alarm.SingletonAlarm;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewAdapter.OnItemClick {

    private static final String TAG = "log";

    private ImageButton mAddAlarmButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SingletonAlarm sAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sAlarms = SingletonAlarm.getInstatce();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAddAlarmButton = (ImageButton) findViewById(R.id.add_alarm_button);
        mAddAlarmButton.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        showAlarms();

//        if (isMyServiceRunning(AlarmService.class))
//            stopAlarmService();
//        startAlarmService();

        Log.d(TAG, "MainActivity onResume() called");
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
        Log.d(TAG, "MainActivity onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        sAlarms.saveAlarms();
        startAlarmService();
        Log.d(TAG, "MainActivity onPause() called");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity onDestroy() called");
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
        mAdapter = new RecyclerViewAdapter(this);
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
        sAlarms.getAlarms().get(position).status = !sAlarms.getAlarms().get(position).status;

        showAlarms();
        Log.v("log", "onItemClickListenerStatus ok");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
