package com.example.android.lesson4alarm;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.lesson4alarm.Fragments.DialodFragmentSetTime;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTime;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ImageButton mAlarmButton;
    Boolean mAlarmState;


    private static final String TAG = "QuizActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTime = (TextView) findViewById(R.id.time);
        mTime.setOnClickListener(this);

        mAlarmButton = (ImageButton) findViewById(R.id.alarm_button);
        mAlarmButton.setOnClickListener(this);

        sharedPref = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mAlarmState = sharedPref.getBoolean("alarm state", false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setAlarmTime();
        setAlarmStateIcon();

        Log.d(TAG, "SignInActivity onResume() called");
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
                break;
        }
    }

    public void setAlarmTime() {
        mTime.setText(sharedPref.getString("alarm time", "12:00"));
    }

    public void writeToSharedPrefAlarmTime(String alarmTime) {
        editor.putString("alarm time", alarmTime);
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

}
