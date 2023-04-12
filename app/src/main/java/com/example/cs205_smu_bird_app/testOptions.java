package com.example.cs205_smu_bird_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.media.MediaPlayer;
import java.util.concurrent.locks.ReentrantLock;

public class testOptions extends AppCompatActivity {
    private Button muteMusic;
    private Button back2Game;
    private MediaPlayer mediaPlayer;
    private boolean isMusicMuted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_testoptions);

        isMusicMuted = getIntent().getBooleanExtra("isMusicMuted", false);      //grab val from parent (yes i tried piping, didnt work)
        muteMusic = (Button) findViewById(R.id.mute_button);
        updateButtonText();     //update value based on initial
        muteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMusicMuted = !isMusicMuted; // Toggle the value of isMusicMuted

//                Intent intent = new Intent(testOptions.this, MainActivity.class);
//                intent.putExtra("isMusicMuted", isMusicMuted);
                updateButtonText();

            }
        });

        back2Game = (Button) findViewById(R.id.back2game);
        back2Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(testOptions.this, MainActivity.class);
                if (muteMusic.getText() == "Unmute Music"){
                        intent.putExtra("isMusicMuted", true);
                }
                else {
                    intent.putExtra("isMusicMuted", false);
                }
                onBackPressed();
            }
        });


    }
    private void updateButtonText() {
        if (isMusicMuted) {
            muteMusic.setText("Unmute Music");
        } else {
            muteMusic.setText("Mute Music");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}