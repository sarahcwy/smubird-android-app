package com.example.cs205_smu_bird_app.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.cs205_smu_bird_app.R;

public class Background implements sprite {
    private int screenHeight;
    private int screenWidth;
    private float backgroundSpeed;
    private float backgroundPosX1, backgroundPosX2;
    Bitmap background; //for setting graphic

    public Background(Resources resources, int screenHeight, int screenWidth, float backgroundSpeed) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.backgroundSpeed = backgroundSpeed;
        backgroundPosX1 = 0;
        backgroundPosX2 = screenWidth;

        Bitmap bkgImage = BitmapFactory.decodeResource(resources, R.drawable.background);
        background = Bitmap.createScaledBitmap(bkgImage, screenWidth, screenHeight, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, backgroundPosX1, 0, null);
        canvas.drawBitmap(background, backgroundPosX2, 0, null);
    }

    @Override
    public void update() {
        backgroundPosX1 -= backgroundSpeed;
        backgroundPosX2 -= backgroundSpeed;

        if (backgroundPosX1 + screenWidth <= 0) {
            backgroundPosX1 = screenWidth;
        }

        if (backgroundPosX2 + screenWidth <= 0) {
            backgroundPosX2 = screenWidth;
        }
    }
}
