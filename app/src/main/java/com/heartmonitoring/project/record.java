package com.heartmonitoring.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class record extends AppCompatActivity {

    private static int MICROPHONE = 200;
    Chronometer chronometer;
    private boolean running;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
       

        setContentView(R.layout.activity_record);

        if (ismicpresent()) {
            getmic();
        }

        chronometer = findViewById(R.id.timer);

    }

    public void start(View v) {

        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
        }

        try {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mediaRecorder.setOutputFile(getpath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Some Error Occurred please restart the App", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    public void stop(View v) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        running=false;
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();

    }

    public void save(View v) {

        Intent intent = new Intent(this, save.class);
        startActivity(intent);

    }

    private boolean ismicpresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getmic() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE);
        }
    }

    private String getpath() {

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "Heartbeat" + ".aac");
        return file.getPath();
    }

}