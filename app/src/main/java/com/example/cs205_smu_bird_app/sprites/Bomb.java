package com.example.cs205_smu_bird_app.sprites;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.cs205_smu_bird_app.R;

import java.util.ArrayList;
import java.util.Random;

public class Bomb implements sprite{
    private int height, width, separation, xPosition, speed;
    private int screenHeight, screenWidth;
    private int headHeight, headExtraWidth;
    private int obstacleMinPosition; //Minimum Position of Obstacle since cannot put at bottom
    //Change obstacleMinPosition if we choose to NOT use ads
    private Bitmap image;
    private BombCallback callback;


    public Bomb(Resources resources, int screenHeight, int screenWidth, BombCallback callback){
        image = BitmapFactory.decodeResource(resources, R.drawable.bomb_character_o_idle);
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.callback = callback;
        xPosition = screenWidth;
        //this.xPosition = (int) (Math.random() * (screenWidth + 1)); // Initialize xPosition with random value
        width = (int) resources.getDimension(R.dimen.obstacle_width);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        separation = (int) resources.getDimension(R.dimen.obstacle_separation);
        headHeight = (int) resources.getDimension(R.dimen.head_height);
        headExtraWidth = (int) resources.getDimension(R.dimen.head_extra_width); //height changes as game progress
        obstacleMinPosition = (int) resources.getDimension(R.dimen.obstacle_min_position);

        //create randomness to bomb
        Random random = new Random(System.currentTimeMillis());
        height = random.nextInt(screenHeight - 2 * obstacleMinPosition - separation) + obstacleMinPosition;

    }


    @Override
    public void draw(Canvas canvas) {
        // Generate random values for vertical placement
        int topHeadTop = (int) (Math.random() * (screenHeight - height - separation - 2 * headHeight + 1));
        int topHeadBottom = topHeadTop + headHeight;
        int bottomHeadTop = topHeadBottom + separation;
        int bottomHeadBottom = bottomHeadTop + headHeight;

        // Update Rect objects with random values
        Rect bottomPipe = new Rect(xPosition + headExtraWidth, screenHeight - height, xPosition + width + headExtraWidth, screenHeight);
        Rect bottomHead = new Rect(xPosition, bottomHeadTop, xPosition + width + 2 * headExtraWidth, bottomHeadBottom);
        Rect topPipe = new Rect(xPosition + headExtraWidth, 0, xPosition + headExtraWidth + width, topHeadTop);
        Rect topHead = new Rect(xPosition, topHeadTop, xPosition + width + 2 * headExtraWidth, topHeadBottom);

        xPosition = (int) (Math.random() * (screenWidth + 1)); // Initialize xPosition with random value
        //fill rectangle so need paint them
        Paint paint = new Paint();
        canvas.drawBitmap(image, null, bottomPipe, paint);
        canvas.drawBitmap(image, null, bottomHead, paint);
        canvas.drawBitmap(image, null, topPipe, paint);
        canvas.drawBitmap(image, null, topHead, paint);
    }

    @Override
    public void update() {

        xPosition -= speed; //move obstacles across the screen
        if (xPosition <= 0 - width - 2*headExtraWidth) {
            callback.bombOffScreen(this);
        } else {
            ArrayList<Rect> positions = new ArrayList<>();
            Rect bottomPosition = new Rect(xPosition, screenHeight - height - headHeight, xPosition + width + 2*headExtraWidth, screenHeight);
            Rect topPosition = new Rect(xPosition, 0, xPosition + width + 2*headExtraWidth, screenHeight - height - headHeight - separation);

            positions.add(bottomPosition);
            positions.add(topPosition);
            callback.updatePosition(this, positions);
        }
    }
}
