package com.example.android.lesson4alarm.Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.android.lesson4alarm.Fragments.DialodFragmentSetTime;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.Services.AlarmService;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTime;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ImageButton mAlarmButton;
    Boolean mAlarmState;
    SimpleDateFormat mTimeFormat;
    Calendar mCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mCalendar = Calendar.getInstance();

        mTime = (TextView) findViewById(R.id.time);
        mTime.setOnClickListener(this);

        mAlarmButton = (ImageButton) findViewById(R.id.alarm_button);
        mAlarmButton.setOnClickListener(this);

        sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();


    }

    @Override
    public void onResume() {
        super.onResume();

        mAlarmState = sharedPref.getBoolean("alarm state", false);
        setAlarmTime();
        setAlarmStateIcon();
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new DialodFragmentSetTime();
        newFragment.show(getFragmentManager(), "time");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time:
                showTimePickerDialog();
                break;
            case R.id.alarm_button:
                writeToSharedPrefAlarmState(!mAlarmState);
                setAlarmStateIcon();
                if (mAlarmState) {
                    startAlarmService();
                } else {
                    stopAlarmService();
                }

                break;
        }
    }

    public void setAlarmTime() {
        int hours = sharedPref.getInt("alarm hour", 12);
        int minutes = sharedPref.getInt("alarm minute", 0);
        mTime.setText(String.format("%02d:%02d", hours, minutes));
    }

    public void writeToSharedPrefAlarmTime(int hourOfDay, int minute) {
        editor.putInt("alarm hour", hourOfDay);
        editor.putInt("alarm minute", minute);
        editor.commit();
    }

    public void writeToSharedPrefAlarmState(Boolean alarmState) {
        mAlarmState = alarmState;
        editor.putBoolean("alarm state", alarmState);
        editor.commit();
    }

    public void setAlarmStateIcon() {
        if (mAlarmState) {
            mAlarmButton.setImageResource(R.mipmap.alarm_active);
        } else {
            mAlarmButton.setImageResource(R.mipmap.alarm_not_active);
        }
    }

    public void startAlarmService() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    public void stopAlarmService() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);
    }

}
