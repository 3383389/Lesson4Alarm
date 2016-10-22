package com.example.android.lesson4alarm.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.android.lesson4alarm.Activity.SetAlarmActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DialodFragmentSetTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    SetAlarmActivity setAlarmActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        setAlarmActivity = (SetAlarmActivity) getActivity();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setDayOfMonth(hourOfDay, minute);
        setAlarmActivity.newAlarm.hourOfDay = hourOfDay;
        setAlarmActivity.newAlarm.minute = minute;
        setAlarmActivity.setAlarmTime();
    }

    // если выбраное время меньше или равняется текущему, дата переносится на следующий день (нужно для одиночных будильников)
    private void setDayOfMonth(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= hourOfDay && calendar.get(Calendar.MINUTE) < minute) {
            setAlarmActivity.newAlarm.DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            setAlarmActivity.newAlarm.DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }


}
