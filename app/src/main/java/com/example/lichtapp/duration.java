package com.example.lichtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class duration extends AppCompatActivity {
    private static final long START_TIME_IN_MILLS =600000;
    private TextView mTextCount;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeft = START_TIME_IN_MILLS;
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
        updateCount();
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft",mTimeLeft);
        outState.putBoolean("timerRunning",mTimerRunning);
        outState.putLong("endTime",mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTimeLeft=savedInstanceState.getLong("milliLeft");
        mTimerRunning=savedInstanceState.getBoolean("timerRunning");
        updateCount();
        updateButtons();

        if(mTimerRunning)
        {
            mEndTime=savedInstanceState.getLong("endTime");
            mTimeLeft=mEndTime-System.currentTimeMillis();
            startTimer();
        }
    }
}