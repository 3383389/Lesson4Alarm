package com.example.android.lesson4alarm;

import java.util.ArrayList;

/**
 * Created by user on 16.10.2016.
 */

public class Alarm {

    public int hourOfDay;
    public int minute;
    public boolean status;
    public boolean isRepeat;
    public int DAY_OF_WEEK[];
    public int DAY_OF_MONTH;

    public Alarm() {
        hourOfDay = 12;
        minute = 0;
        status = false;
        isRepeat = false;
        DAY_OF_WEEK = new int[]{
                0, 0, 0, 0, 0, 0, 0
        };
        DAY_OF_MONTH = 0;
    }


}
