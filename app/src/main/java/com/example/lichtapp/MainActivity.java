package com.example.lichtapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView stopwatch = findViewById(R.id.stopwatch);
        stopwatch.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,stopwatch.class)));
        CardView duration= findViewById(R.id.duration);
        duration.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,duration.class)));
    }
}