package com.example.java_oglen.urunyonetimi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    ImageView imgLogo;
    public Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // animasyon
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animscreen);
        animation.setAnimationListener(this);
        imgLogo.startAnimation(animation);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        /*
        Timer tm = new Timer();
        TimerTask is = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
            }
        };
        tm.schedule(is, 1000);
        */


    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
