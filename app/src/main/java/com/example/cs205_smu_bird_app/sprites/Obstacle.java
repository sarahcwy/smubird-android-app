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

public class Obstacle implements sprite{
    private int height, width, separation, xPosition, speed;
    private int screenHeight, screenWidth;
    private int headHeight, headExtraWidth;
    private int obstacleMinPosition; //Minimum Position of Obstacle since cannot put at bottom
    //Change obstacleMinPosition if we choose to NOT use ads
    private Bitmap image;
    private ObstacleCallback callback;


    public Obstacle(Resources resources, int screenHeight, int screenWidth, ObstacleCallback callback){
        image = BitmapFactory.decodeResource(resources, R.drawable.testpipe); //changed from pipes to testpipe
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.callback = callback;
        xPosition = screenWidth;
        width = (int) resources.getDimension(R.dimen.obstacle_width);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        separation = (int) resources.getDimension(R.dimen.obstacle_separation);
        headHeight = (int) resources.getDimension(R.dimen.head_height);
        headExtraWidth = (int) resources.getDimension(R.dimen.head_extra_width); //height changes as game progress
        obstacleMinPosition = (int) resources.getDimension(R.dimen.obstacle_min_position);


        //create randomness to obstacle
        Random random = new Random(System.currentTimeMillis());
        height = random.nextInt(screenHeight - 2 * obstacleMinPosition - separation) + obstacleMinPosition;

    }


    @Override
    public void draw(Canvas canvas) {
        //use rectangle to fill Image, NOT BITMAP
        //because rectangle png is a rectangle
        Rect bottomPipe = new Rect(xPosition + headExtraWidth, screenHeight - height, xPosition + width + headExtraWidth, screenHeight);
        Rect bottomHead = new Rect(xPosition, screenHeight - height - headHeight, xPosition + width + 2 * headExtraWidth, screenHeight - height);
        Rect topPipe = new Rect(xPosition + headExtraWidth, 0, xPosition + headExtraWidth + width, screenHeight-height-separation-2*headHeight);
        Rect topHead = new Rect(xPosition, screenHeight - height - separation - 2*headHeight, xPosition + width + 2 *headExtraWidth, screenHeight - height - separation - headHeight);

        //fill rectangle so need pain them
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
            callback.obstacleOffScreen(this);
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
