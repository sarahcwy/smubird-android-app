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


        back2Game = (Button) findViewById(R.id.back2game);
        back2Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendResultToMainActivity(isMusicMuted);
                onBackPressed();
            }
        });

        isMusicMuted = getIntent().getBooleanExtra("isMusicMuted", false);      //grab val from parent (yes i tried piping, didnt work)

        muteMusic = (Button) findViewById(R.id.mute_button);
        updateButtonText();     //update value based on initial

        muteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMusicMuted = !isMusicMuted; // Toggle the value of isMusicMuted
                updateButtonText(); // Update button text

                if (isMusicMuted) {
                    // Pause the music if muted
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } else {
                    // Start the music if unmuted
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }

                // Return the updated value of isMusicMuted to parent activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isMusicMuted", isMusicMuted);
                setResult(RESULT_OK, resultIntent);
                //finish(); // Finish the options activity
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

    private void sendResultToMainActivity(boolean isMusicMuted) {
        Intent intent = new Intent();
        intent.putExtra("isMusicMuted", isMusicMuted); // Pass the updated value of isMusicMuted as a result
        setResult(RESULT_OK, intent); // Set the result as RESULT_OK
        finish(); // Finish the activity and return to MainActivity
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("isMusicMuted", isMusicMuted); // Pass the updated value of isMusicMuted to parent activity
        setResult(RESULT_OK, resultIntent); // Set the result to RESULT_OK and pass the result intent
        super.onBackPressed();
    }
}