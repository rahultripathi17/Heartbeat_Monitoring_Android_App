package com.heartmonitoring.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        

        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.logut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                new SessionManager(MainActivity.this).logoutSession();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    public void openrecord(View view){
        Intent intent=new Intent(this,record.class);
        startActivity(intent);
    }

    public void openlisten(View view){
       Intent intent=new Intent(this,listen.class);
        startActivity(intent);

    }

    public  void  opencredits(View view){

        Intent intent=new Intent(this,credits.class);
        startActivity(intent);

    }

}