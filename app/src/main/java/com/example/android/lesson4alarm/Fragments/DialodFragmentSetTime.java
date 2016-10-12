package com.example.android.lesson4alarm.Fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;


import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.lesson4alarm.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.String.format;


public class DialodFragmentSetTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    SimpleDateFormat timeFormat;
    MainActivity mainActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        mainActivity = (MainActivity) getActivity();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mainActivity.writeToSharedPrefAlarmTime(String.format("%02d:%02d", hourOfDay, minute));
        mainActivity.setAlarmTime();
        mainActivity.writeToSharedPrefAlarmState(true);
        mainActivity.setAlarmStateIcon();
    }
}
