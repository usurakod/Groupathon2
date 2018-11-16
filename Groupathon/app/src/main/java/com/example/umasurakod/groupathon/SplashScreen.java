package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.umasurakod.groupathon.AccountActivity.LoginActivity;
import com.example.umasurakod.groupathon.AccountActivity.SignupActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        VideoView video1=findViewById(R.id.splash_video);
        String VideoPath="android.resource://"+getPackageName()+"/"+R.raw.video4;
        Uri uri = Uri.parse(VideoPath);
        video1.setVideoURI(uri);

        video1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }

        });
        video1.start();


    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }
}
