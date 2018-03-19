package com.chrysalis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.chrysalis.R;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;

/**
 * Created by Bitware Marketing on 22-05-2017.
 */

public class SplashActivity extends Activity {
    public static int a = 10;
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Config.strIsEditCalled = "No";

        sharedPreferences = SplashActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                System.out.println("*******splash1**********" + sharedPreferences.getString("isLogin", ""));
                if (sharedPreferences.getString("isLogin", "").equalsIgnoreCase("Yes")) {
                    System.out.println("*******splash1**********");
                    //sp = SplashActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

//Save to SharedPreferences
                    sharedPreferences.edit().putString("KEY", "empty").commit();
                    System.out.println("*******splash2**********" + sharedPreferences.getString("KEY", ""));

                    Intent i = new Intent(SplashActivity.this, ProductListingScreen.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(SplashActivity.this, WelcomeSlider.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }

}
