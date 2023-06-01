package com.example.lichtapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class endtime extends AppCompatActivity {
    private static final long START_TIME_IN_MILLS =600000;

    private EditText mEditTextInput;
    private TextView mTextCount;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeft;
    private long mStartTimeInMillis ;

    private long mEndTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endtime);

        mEditTextInput=findViewById(R.id.edit_text_input);
        mTextCount= findViewById(R.id.text_count);
        mButtonSet=findViewById(R.id.button_set);
        mButtonStartPause=findViewById(R.id.button_start_pause);
        mButtonReset=findViewById(R.id.button_reset);

        mButtonSet.setOnClickListener(view -> {
            String input = mEditTextInput.getText().toString();
            if (input.length()==0){
                Toast.makeText(endtime.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            long millisInput=Long.parseLong(input) * 60000;
            if(millisInput==0){
                Toast.makeText(endtime.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                return;
            }
            setTime(millisInput);
            mEditTextInput.setText("");
        });

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
    private void setTime(long millisec)
    {
        mStartTimeInMillis=millisec;
        resetTimer();
        closeKey();
    }
    @SuppressLint("SetTextI18n")
    private void startTimer()
    {
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
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);

            }
        }.start();
        mTimerRunning=true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    @SuppressLint("SetTextI18n")
    private void pauseTimer()
    {
        mCountDownTimer.cancel();
        mTimerRunning=false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer()
    {
        updateCount();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCount()
    {
        int hours=(int) (mTimeLeft/1000) / 3600;
        int min=(int) ((mTimeLeft/1000)%3600)/60;
        int sec=(int) (mTimeLeft/1000)%60;

        String timeLeftFormatted;
        if(hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d",hours,min,sec);
        }else{
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d",min,sec);
        }
        mTextCount.setText(timeLeftFormatted);
    }

    @SuppressLint("SetTextI18n")
    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);

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
    private void closeKey(){
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong("startTimeInMillis",mStartTimeInMillis);
        editor.putLong("millisLeft",mTimeLeft);
        editor.putBoolean("timerRunning",mTimerRunning);
        editor.putLong("endTime",mEndTime);

        editor.apply();
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);

        mStartTimeInMillis=pref.getLong("startTimeInMillis",600000);

        mTimeLeft=pref.getLong("millisLeft",START_TIME_IN_MILLS);
        mTimerRunning=pref.getBoolean("timerRunning",false);

        updateCount();
        updateWatchInterface();

        if(mTimerRunning) {
            mEndTime=pref.getLong("endTime",0);
            mTimeLeft=mEndTime-System.currentTimeMillis();

            if(mTimeLeft < 0){
                mTimeLeft=0;
                mTimerRunning=false;
                updateCount();
                updateWatchInterface();
            }else{
                startTimer();
            }
        }
    }
}



