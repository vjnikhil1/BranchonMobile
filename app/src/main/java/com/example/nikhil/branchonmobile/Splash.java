package com.example.nikhil.branchonmobile;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    Animation animation;
    ImageView logo;
    TextView caption;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
//        TextView textView = (TextView) findViewById(R.id.textView28);
        logo = (ImageView) findViewById(R.id.imageView3);
        caption = (TextView) findViewById(R.id.textView30);
        pb = (ProgressBar) findViewById(R.id.progressBar3);
//        logo.setAnimation(animation);
        caption.setAnimation(animation);
//        textView.setAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
            }
        }, 2000);
    }
}
