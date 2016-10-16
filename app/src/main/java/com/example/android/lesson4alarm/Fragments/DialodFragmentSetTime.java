package com.example.android.lesson4alarm.Fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import com.example.android.lesson4alarm.Activity.MainActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DialodFragmentSetTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    SimpleDateFormat mTimeFormat;
    MainActivity mainActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        mainActivity = (MainActivity) getActivity();

        mTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        mainActivity.writeToSharedPrefAlarmTime(hourOfDay, minute);
        mainActivity.setAlarmTime();
        mainActivity.writeToSharedPrefAlarmState(true);
        mainActivity.setAlarmStateIcon();
        mainActivity.startAlarmService();
    }


    
}
