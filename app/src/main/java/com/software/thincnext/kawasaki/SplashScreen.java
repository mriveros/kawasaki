package com.software.thincnext.kawasaki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.software.thincnext.kawasaki.Activity.HomeActivity;
import com.software.thincnext.kawasaki.Activity.Register;
import com.software.thincnext.kawasaki.Services.Constants;


public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();
    // handler
    private Handler mHanlder = new Handler();
    private SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // shared prefernece declaration
        mPref = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //handle function
        mHanlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean isLoggedIn = mPref.getBoolean(Constants.IS_LOGGED_IN, false);
                if (isLoggedIn) {
                    //go to otp page
                    Intent intent = new Intent(SplashScreen.this,Register.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // go to home page
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }

        }, 2000);
    }


    }

