package com.heartmonitoring.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.view.Window;
import android.os.Bundle;
import android.os.Handler;

import com.heartmonitoring.project.R;

public class loading extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().setStatusBarColor(ContextCompat.getColor(loading.this, R.color.status2));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(SplashActivity.this,LoginActivity.class ));
                startActivity(new Intent(loading.this,LoginActivity.class ));
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}