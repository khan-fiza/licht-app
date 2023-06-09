package com.example.lichtapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class duration extends AppCompatActivity {
    private static final long START_TIME_IN_MILLS =600000;
    private TextView mTextCount;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeft;
    private long mEndTime;

    public duration() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);

        mTextCount= findViewById(R.id.text_count);
        mButtonStartPause=findViewById(R.id.button_start_pause);
        mButtonReset=findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(view -> {
            if(mTimerRunning)
            {
                pauseTimer();
            }
            else{
                startTimer();
            }

        });
        mButtonReset.setOnClickListener(view -> resetTimer());

    }
    @SuppressLint("SetTextI18n")
    private void startTimer()
    {
        mEndTime=System.currentTimeMillis()+mTimeLeft;
        mCountDownTimer = new CountDownTimer(mTimeLeft,1000) {
            @Override
            public void onTick(long millisUntilFin) {
                mTimeLeft=millisUntilFin;
                updateCount();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                mTimerRunning=false;
                updateButtons();

            }
        }.start();
        mTimerRunning=true;
        updateButtons();
    }
    @SuppressLint("SetTextI18n")
    private void pauseTimer()
    {
        mCountDownTimer.cancel();
        mTimerRunning=false;
        updateButtons();
    }
    private void resetTimer()
    {
        mTimeLeft=START_TIME_IN_MILLS;
        updateCount();
        updateButtons();
    }
    private void updateCount()
    {
        int min=(int) (mTimeLeft/1000)/60;
        int sec=(int) (mTimeLeft/1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        mTextCount.setText(timeLeftFormatted);

    }
    @SuppressLint("SetTextI18n")
    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");
            if (mTimeLeft < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
            if (mTimeLeft < START_TIME_IN_MILLS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong("millisLeft",mTimeLeft);
        editor.putBoolean("timerRunning",mTimerRunning);
        editor.putLong("endTime",mEndTime);

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);

        mTimeLeft=pref.getLong("millisLeft",START_TIME_IN_MILLS);
        mTimerRunning=pref.getBoolean("timerRunning",false);

        updateCount();
        updateButtons();

        if(mTimerRunning) {
            mEndTime=pref.getLong("endTime",0);
            mTimeLeft=mEndTime-System.currentTimeMillis();

            if(mTimeLeft < 0){
                mTimeLeft=0;
                mTimerRunning=false;
                updateCount();
                updateButtons();
            }else{
                startTimer();
            }
        }
    }
}