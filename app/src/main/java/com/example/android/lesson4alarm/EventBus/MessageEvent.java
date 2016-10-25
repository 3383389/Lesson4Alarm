package com.example.android.lesson4alarm.EventBus;

import com.example.android.lesson4alarm.Alarm;

public class MessageEvent {
    public final Messages message;
    public final Alarm alarm;
    public final Integer position;

    public MessageEvent(Messages message, Alarm alarm, Integer position) {
        this.message = message;
        this.alarm = alarm;
        this.position = position;
    }

    public MessageEvent(Messages message, Alarm alarm) {
        this.message = message;
        this.alarm = alarm;
        this.position = null;
    }

    public MessageEvent(Messages message, Integer position) {
        this.message = message;
        this.alarm = null;
        this.position = position;
    }

    public MessageEvent(Messages message) {
        this.message = message;
        this.alarm = null;
        this.position = null;
    }

}