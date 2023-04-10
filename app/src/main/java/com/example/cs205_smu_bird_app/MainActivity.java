package com.example.cs205_smu_bird_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import android.media.MediaPlayer;

public class MainActivity extends Activity {
    private MediaPlayer mediaPlayer;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Initialize media player
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bgm);
                mediaPlayer.setLooping(true); // Set audio to loop
                mediaPlayer.start(); // Start audio playback
            }
        });
        audioThread.start(); // Start the audio thread




        MobileAds.initialize(this, "ca-app-pub-9057526686789846~7828440247");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause audio playback
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Resume audio playback
        }
    }

}