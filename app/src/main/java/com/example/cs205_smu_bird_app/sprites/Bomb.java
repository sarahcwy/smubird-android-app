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
        height = random.nextInt(screenHeight - 1 * obstacleMinPosition - separation) + obstacleMinPosition;

    }


    @Override
    public void draw(Canvas canvas) {
        int bombTop = height;

        Rect bomb = new Rect(xPosition + headExtraWidth, bombTop, xPosition + width + headExtraWidth, screenHeight);

        Paint paint = new Paint();
        canvas.drawBitmap(image, null, bomb, paint);
    }

    public void update() {
        xPosition -= speed; // Move obstacles across the screen
        if (xPosition <= 0 - width - 2 * headExtraWidth) {
            callback.bombOffScreen(this);
        } else {
            ArrayList<Rect> positions = new ArrayList<>();
            Rect bombPosition = new Rect(xPosition, height, xPosition + width + 2 * headExtraWidth, screenHeight);
            positions.add(bombPosition);
            callback.updatePosition(this, positions);
        }
    }
}
