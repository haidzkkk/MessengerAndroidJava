package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplahScreenActivity2 extends AppCompatActivity {
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen2);

        isLogin =  getSharedPreferences("dataUser", MODE_PRIVATE).getBoolean("isLogin", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin)    {
                    startActivity(new Intent(SplahScreenActivity2.this, MainActivity.class));
                }
                else{
                    startActivity(new Intent(SplahScreenActivity2.this, LogInActivity.class));
                }
                finishAffinity();
            }
        }, 1000);
    }
}