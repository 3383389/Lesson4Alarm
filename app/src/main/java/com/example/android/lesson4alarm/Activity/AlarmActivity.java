package com.example.android.lesson4alarm.Activity;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.android.lesson4alarm.R;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    Button mOffAlarmButton;
    Vibrator mVibro;

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

        mVibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 200, 100, 300, 300};
        mVibro.vibrate(pattern, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alrm_off_button:
                mVibro.cancel();
                finish();
                break;
        }
    }
}
