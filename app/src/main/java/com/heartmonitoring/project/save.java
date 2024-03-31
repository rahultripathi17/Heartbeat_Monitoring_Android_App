package com.heartmonitoring.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.File;
import java.util.Calendar;

public class save extends AppCompatActivity {

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    StorageReference storage;
    EditText editText;
    TextView status;
    ProgressDialog progressDialog;
    String uid;
    String note;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        setContentView(R.layout.activity_save);
        progressDialog = new ProgressDialog(save.this);
        editText = findViewById(R.id.filename);
        status = findViewById(R.id.status);
        progressDialog.setMessage("Loading");
        uid = new SessionManager(save.this).getUsersDetailsFromSessions().get(SessionManager.KEY_UID);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void upload(View v) {

        name = editText.getText().toString();
        note = ((EditText) findViewById(R.id.note)).getText().toString();

        if (name.trim().isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            name = dtf.format(now);
            name = name.replace("/", "_");
            name = name.replace(":", "_");

        }

        if (note.trim().isEmpty()) {
            note = "No Note";

        }


        progressDialog.show();
        storage = FirebaseStorage.getInstance().getReference("AUDIO").child(name);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "Heartbeat" + ".aac");


        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/aac")
                .setCustomMetadata("fileName", name)
                .build();


        String finalName = name;
        storage.putFile(Uri.fromFile(file), metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String temp = uri.toString();


                            RecordingModel recordingModel = new RecordingModel();
                            recordingModel.setName(name);
                            recordingModel.setId(FirebaseDatabase.getInstance().getReference("users").child(uid).child("audioFiles").push().getKey());
                            recordingModel.setTimeStamp(Calendar.getInstance().getTime().getTime());
                            recordingModel.setNote(note);
                            recordingModel.setLink(temp);

                            FirebaseDatabase.getInstance().getReference("users").child(uid).child("audioFiles").child(recordingModel.getId()).setValue(recordingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        status.setText("File Successfully Uploaded");
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(save.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });

                        }
                    });
                    Toast.makeText(save.this, "File Successfully Uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}