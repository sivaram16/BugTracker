package com.example.sivaram.bugtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    private String uid;
    private Boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        uid =pref.getString("UID", null); // getting String
        admin=pref.getBoolean("Admin", false); // getting boolean
        Thread myThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    /*Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);*/
                    if (uid!=null) {
                        Intent intent =new Intent(Splash.this,MainActivity.class);
                        intent.putExtra("UID",uid);
                        intent.putExtra("Admin",admin);
                        startActivity(intent);
                    }
                    else {
                        Intent intent =new Intent(Splash.this,Login.class);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }




    }
}
