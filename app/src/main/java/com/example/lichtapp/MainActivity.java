package com.example.lichtapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView exit = findViewById(R.id.exit);
        exit.setOnClickListener(view -> {
            SharedPreferences.Editor editor= sharedpreferences.edit();
            editor.clear();
            editor.apply();
        });

        CardView stopwatch = findViewById(R.id.stopwatch);
        stopwatch.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,stopwatch.class)));
        CardView duration= findViewById(R.id.duration);
        duration.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,duration.class)));

    }
}