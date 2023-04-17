package com.example.cs205_smu_bird_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import android.media.MediaPlayer;
import android.widget.Button;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private Button buttonOptions;

    private AdView mAdView;
    private boolean isMusicMuted = false;

    private static final int REQUEST_CODE_TEST_OPTIONS = 1;

    public void openOptions() {
        Intent intent = new Intent(this, testOptions.class);
        intent.putExtra("isMusicMuted", isMusicMuted);
        startActivityForResult(intent, REQUEST_CODE_TEST_OPTIONS);          //initialises activity options (separate screen)
    }

// waits for the onCLick response from options
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TEST_OPTIONS && resultCode == RESULT_OK) {
            isMusicMuted = data.getBooleanExtra("isMusicMuted", isMusicMuted);
            updateAudioPlayback();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Screen always on with this line
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        buttonOptions = (Button) findViewById(R.id.buttonOptions);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.settings);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        buttonOptions.setBackground(drawable);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptions();
            }
        });

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bgm2);
        //setting up and playing of BGM
        mediaPlayer.setLooping(true);
        updateAudioPlayback();

        MobileAds.initialize(this, "ca-app-pub-9057526686789846~7828440247");
        //addition of google ad code with the test key
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //loading of googleAD
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
        if (!isMusicMuted && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAudioPlayback();
    }

    private void updateAudioPlayback() {
        if (mediaPlayer != null) {
            if (isMusicMuted) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }
}
