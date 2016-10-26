package com.example.android.lesson4alarm.Activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.EventBus.MessageEvent;
import com.example.android.lesson4alarm.EventBus.Messages;
import com.example.android.lesson4alarm.Fragments.DialodFragmentSetTime;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.SingletonAlarm;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private TextView mSetTime;
    private ToggleButton monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private Button mSetAlarm, mCancel, mDelete;
    private TextView mTimeTextView;
    private SingletonAlarm sAlarms;
    public Alarm newAlarm;
    private Intent intent;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        initVariables();

        // если активность открыта с данными о позиции будильника - редактируем существующий будильник,
        // если без данных создаем новый
        if (isEditAlarm()) {
            newAlarm = sAlarms.getAlarms().get(position);
            setStatusOfButtonsDayOdWeek();
            Log.v("log", "redactor alarm ok" + position);
        } else {
            newAlarm = new Alarm();
        }

        setAlarmTime();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAlarmTime();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_time_alarm:
                showTimePickerDialog();
                break;
            case R.id.set_button_set_alarm:
                setRepeatedStatus();
                newAlarm.status = true;
                if (isEditAlarm()) {
                    EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_ALARM, newAlarm, position));
                } else {
                    EventBus.getDefault().post(new MessageEvent(Messages.ADD_ALARM, newAlarm));
                }
                finish();
                break;
            case R.id.cancel_button_set_alarm:
                finish();
                break;
            case R.id.delete_alarm:
                if (isEditAlarm()) {
                    EventBus.getDefault().post(new MessageEvent(Messages.DELETE_ALARM, position));
                }
                finish();
                break;
        }
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new DialodFragmentSetTime();
        newFragment.show(getFragmentManager(), "time");
    }

    public void setAlarmTime() {
        mTimeTextView.setText(String.format("%02d:%02d", newAlarm.hourOfDay, newAlarm.minute));
    }

    public boolean isEditAlarm() {
        if (position != -1)
            return true;
        else
            return false;
    }

    public void setRepeatedStatus() {
        for (int i = 0; i < 7; i++) {
            if (newAlarm.DAY_OF_WEEK[i] != 0) {
                newAlarm.isRepeat = true;
                setDayOfMonthForRepeatedAlarm();
                break;
            } else {
                newAlarm.isRepeat = false;
            }
        }
    }

    private void setDayOfMonthForRepeatedAlarm() {
        Calendar c = Calendar.getInstance();
        if (newAlarm.hourOfDay < c.get(Calendar.HOUR_OF_DAY) || (newAlarm.hourOfDay == c.get(Calendar.HOUR_OF_DAY) && newAlarm.minute <= c.get(Calendar.MINUTE))) {
            newAlarm.DAY_OF_MONTH = c.get(Calendar.DAY_OF_MONTH);
        } else {
            newAlarm.DAY_OF_MONTH = 0;
        }
    }

    // запоминаем дни для повторения
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.monday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[0] = 2;
                } else {
                    newAlarm.DAY_OF_WEEK[0] = 0;
                }
                break;
            case R.id.tuesday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[1] = 3;
                } else {
                    newAlarm.DAY_OF_WEEK[1] = 0;
                }
                break;
            case R.id.wednesday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[2] = 4;
                } else {
                    newAlarm.DAY_OF_WEEK[2] = 0;
                }
                break;
            case R.id.thursday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[3] = 5;
                } else {
                    newAlarm.DAY_OF_WEEK[3] = 0;
                }
                break;
            case R.id.friday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[4] = 6;
                } else {
                    newAlarm.DAY_OF_WEEK[4] = 0;
                }
                break;
            case R.id.saturday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[5] = 7;
                } else {
                    newAlarm.DAY_OF_WEEK[5] = 0;
                }
                break;
            case R.id.sunday_button:
                if (isChecked) {
                    newAlarm.DAY_OF_WEEK[6] = 1;
                } else {
                    newAlarm.DAY_OF_WEEK[6] = 0;
                }
                break;
        }
    }

    public void setStatusOfButtonsDayOdWeek() {

        if (newAlarm.DAY_OF_WEEK[0] != 0) {
            monday.setChecked(true);
        } else {
            monday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[1] != 0) {
            tuesday.setChecked(true);
        } else {
            tuesday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[2] != 0) {
            wednesday.setChecked(true);
        } else {
            wednesday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[3] != 0) {
            thursday.setChecked(true);
        } else {
            thursday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[4] != 0) {
            friday.setChecked(true);
        } else {
            friday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[5] != 0) {
            saturday.setChecked(true);
        } else {
            saturday.setChecked(false);
        }
        if (newAlarm.DAY_OF_WEEK[6] != 0) {
            sunday.setChecked(true);
        } else {
            sunday.setChecked(false);
        }

    }

    private void initVariables() {
        sAlarms = SingletonAlarm.getInstatce();

        mSetTime = (TextView) findViewById(R.id.set_time_alarm);
        mSetAlarm = (Button) findViewById(R.id.set_button_set_alarm);
        mCancel = (Button) findViewById(R.id.cancel_button_set_alarm);
        mDelete = (Button) findViewById(R.id.delete_alarm);
        mTimeTextView = (TextView) findViewById(R.id.set_time_alarm);
        monday = (ToggleButton) findViewById(R.id.monday_button);
        tuesday = (ToggleButton) findViewById(R.id.tuesday_button);
        wednesday = (ToggleButton) findViewById(R.id.wednesday_button);
        thursday = (ToggleButton) findViewById(R.id.thursday_button);
        friday = (ToggleButton) findViewById(R.id.friday_button);
        saturday = (ToggleButton) findViewById(R.id.saturday_button);
        sunday = (ToggleButton) findViewById(R.id.sunday_button);

        mSetAlarm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mSetTime.setOnClickListener(this);
        monday.setOnCheckedChangeListener(this);
        tuesday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);

        intent = getIntent();
        position = intent.getIntExtra("position", -1);
    }

}
