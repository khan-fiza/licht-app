package com.example.lichtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class stopwatch extends AppCompatActivity {

    private int sec;
    private boolean running;
    private boolean wasRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        if(savedInstanceState !=null){
           savedInstanceState.getInt( "sec");
           savedInstanceState.getBoolean( "running");
           savedInstanceState.getBoolean( "wasRunning");
        }

        runTimer();
    }
    public void onStart(View view){
        running = true;
    }

    public void onStop(View view){
        running = false;

    }
    public void onReset(View view)
    {
        running=false;
        sec=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasRunning){
            running=true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("sec",sec);
        outState.putBoolean("running",running);
        outState.putBoolean("wasRunning",wasRunning);

    }

    private void runTimer() {
        TextView timeView = findViewById(R.id.textview);
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hour = sec / 3600;
                int min = (sec % 3600) / 60;
                int secs = sec % 60;

                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d",
                        hour, min, secs);
                timeView.setText(time);
                if (running) {
                    sec++;
                }
                handler.postDelayed(this, 1000);


            }
        });

    }
}