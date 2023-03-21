package com.example.cs205_smu_bird_app.sprites;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;

import com.example.cs205_smu_bird_app.R;

public class GameOver implements sprite{
    private Bitmap gameOver;
    private int screenHeight, screenWidth;
    public GameOver(Resources resources, int screenHeight, int screenWidth){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        gameOver = BitmapFactory.decodeResource(resources, R.drawable.gameover);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(gameOver, screenWidth/2 - gameOver.getWidth() /2 , screenHeight / 4, null);
    }

    @Override
    public void update() {

    }
}
