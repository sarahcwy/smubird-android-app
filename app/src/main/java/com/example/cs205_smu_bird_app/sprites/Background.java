package com.example.cs205_smu_bird_app.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.cs205_smu_bird_app.R;

public class Background implements sprite {
    private int screenHeight;
    private int screenWidth;                //dimen of screen
    private float backgroundSpeed;          //determine how fast the background moves
    private float backgroundPosX1, backgroundPosX2;     //x coords of instances of background image
    Bitmap background; //for setting graphic

    public Background(Resources resources, int screenHeight, int screenWidth, float backgroundSpeed) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.backgroundSpeed = backgroundSpeed;
        backgroundPosX1 = 0;
        backgroundPosX2 = screenWidth;

        Bitmap bkgImage = BitmapFactory.decodeResource(resources, R.drawable.background);       //decode resource to create image
        background = Bitmap.createScaledBitmap(bkgImage, screenWidth, screenHeight, false); //scale to screen size as above to fit
        // diff devices on diff modes (landscape or portrait)
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, backgroundPosX1, 0, null);
        canvas.drawBitmap(background, backgroundPosX2, 0, null);
        //draw background image twice side by side
        //This implementation allows us to create illusion of continuous scrolling background
    }

    @Override
    public void update() {
        backgroundPosX1 -= backgroundSpeed;
        backgroundPosX2 -= backgroundSpeed;
//The above update xCoords of background image based on backgroundSpeed
        if (backgroundPosX1 + screenWidth <= 0) {
            backgroundPosX1 = screenWidth;
        }

        if (backgroundPosX2 + screenWidth <= 0) {
            backgroundPosX2 = screenWidth;
        }
        //If image go beyond screenBoundary, reset position to create looping effect


    }
}
