package com.example.dhoni.authregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Intent i = new Intent(MainActivity.this,SelectCategory.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        },5*1000);

    }
    }
