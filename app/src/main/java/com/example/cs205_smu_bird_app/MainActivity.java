    package com.example.cs205_smu_bird_app;

    import androidx.appcompat.app.AppCompatActivity;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.view.Window;

    import com.google.android.gms.ads.AdRequest;
    import com.google.android.gms.ads.AdView;
    import com.google.android.gms.ads.MobileAds;
    import android.media.MediaPlayer;
    import android.widget.Button;

    public class MainActivity extends Activity {
        private MediaPlayer mediaPlayer;
        private Button button;

        private AdView mAdView;             //variable for googleads(test) check how to replace with own
        private boolean isMusicMuted = false; // Flag to keep track of music mute status


        public void openOptions(){
            Intent intent = new Intent(this, testOptions.class);
            intent.putExtra("isMusicMuted", isMusicMuted); // Pass the current value of isMusicMuted to child activity
            startActivity(intent);  //start activity and wait for result
        }
        public boolean getMusicResult(){
            Intent intent = getIntent();
            return intent.getBooleanExtra("isMusicMuted", isMusicMuted);
        }


        @Override       //load activity
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);

            button = (Button) findViewById(R.id.buttonOptions);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openOptions();
                }
            });


            Thread audioThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Initialize media player
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bgm);
                    mediaPlayer.setLooping(true); // Set audio to loop

                    boolean musicResult= getMusicResult();

                    if (musicResult == true) {
                        mediaPlayer.pause();
                    }
                    else {
                        mediaPlayer.start();
                    }

                }
            });
            audioThread.run(); // Start the audio thread
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