package com.heartmonitoring.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class listen extends AppCompatActivity {

    String uid;
    RecyclerView rv;


    ArrayList<RecordingModel> list;
    RecordingAdapter recordingAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = new SessionManager(listen.this).getUsersDetailsFromSessions().get(SessionManager.KEY_UID);
        setContentView(R.layout.activity_listen);


        progressDialog = new ProgressDialog(listen.this);
        progressDialog.setMessage("Loading");


        list = new ArrayList<>();
        recordingAdapter = new RecordingAdapter(listen.this, list, new RecordingAdapter.RecordingClickedListener() {
            @Override
            public void onRecordingClicked(RecordingModel rm) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(rm.getLink());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(listen.this);
                alertDialogBuilder.setMessage("Do you want to stop playing this audio file?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        dialog.dismiss();
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        rv = findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(listen.this));
        rv.setHasFixedSize(true);
        rv.setAdapter(recordingAdapter);

        progressDialog.show();

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("audioFiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot audioFilesSnap) {

                if (audioFilesSnap.exists()) {

                    for (DataSnapshot file : audioFilesSnap.getChildren()) {

                        list.add(file.getValue(RecordingModel.class));

                    }
                    Collections.sort(list,(a,b)-> b.getTimeStamp().compareTo(a.getTimeStamp()));

                    ((ImageView) findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(listen.this, PdfGenerateActivity.class).putExtra("list", (ArrayList<RecordingModel>) list));
                        }
                    });

                    recordingAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(listen.this, "No recordings present", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}