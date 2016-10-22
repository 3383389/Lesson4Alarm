package com.example.android.lesson4alarm.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.Services.AlarmService;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    Button mOffAlarmButton;
    Vibrator mVibro;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        mOffAlarmButton = (Button) findViewById(R.id.alrm_off_button);
        mOffAlarmButton.setOnClickListener(this);

        player = MediaPlayer.create(this, R.raw.hey_oh);
        player.setLooping(true);
        player.start();

        mVibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 200, 100, 300, 300};
        mVibro.vibrate(pattern, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alrm_off_button:
                mVibro.cancel();
                player.stop();
                startAlarmService();
                finish();
                break;
        }
    }

    public void startAlarmService() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }
}
