package com.example.cs205_smu_bird_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

public class testOptions extends AppCompatActivity {
    private Button muteMusic;
    private Button back2Game;
    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private boolean isMusicMuted;

    private Runnable muteRunnable = new Runnable() {
        @Override
        public void run() {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    };

    private Runnable unmuteRunnable = new Runnable() {
        @Override
        public void run() {
            int currentVolume = volumeSeekBar.getProgress();
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_testoptions);

        Intent intent = getIntent();
        isMusicMuted = intent.getBooleanExtra("isMusicMuted", false);

        muteMusic = (Button) findViewById(R.id.mute_button);
        updateButtonText();
        muteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMusicMuted = !isMusicMuted;
                if (isMusicMuted) {
                    new Thread(muteRunnable).start();
                } else {
                    new Thread(unmuteRunnable).start();
                }
                updateButtonText();
            }
        });

        back2Game = (Button) findViewById(R.id.back2game);
        back2Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed2();
            }
        });

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //volume slider bar to control volume via touchEvent
        volumeSeekBar = (SeekBar) findViewById(R.id.volume_seekbar);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!isMusicMuted) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateButtonText() {
        if (isMusicMuted) {
            muteMusic.setText("Unmute Music");
        } else {
            muteMusic.setText("Mute Music");
        }
    }

    public void onBackPressed2() {
        Intent intent = new Intent();
        intent.putExtra("isMusicMuted", isMusicMuted);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
