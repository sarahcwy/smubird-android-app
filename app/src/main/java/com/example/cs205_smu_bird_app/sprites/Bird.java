package com.example.cs205_smu_bird_app.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cs205_smu_bird_app.GameManagerCallback;
import com.example.cs205_smu_bird_app.R;

public class Bird implements sprite{

    //create a map
    private Bitmap bird_down;
    private Bitmap bird_up;
    private int birdY, birdX, birdWidth, birdHeight; //Scale according to size of device
    //prevent image look excessively small or big
    //Go to Res > values > dimens.xml to fiddle with the values
    private float gravity; //adds gravity to bird

    private float currentFallingSpeed; //current fallSpeed of bird
    private float flappyBoost; //add speed to bird everytime we tap
    private boolean collision = false;
    private int screenHeight;
    private GameManagerCallback callback;

    //bird Constructor
    public Bird(Resources resources, int screenHeight, GameManagerCallback callback) {
        this.screenHeight = screenHeight;
        this.callback = callback;
        //xPos of bird with instantiation of integer since float
        birdX = (int) resources.getDimension(R.dimen.bird_x);
        birdY = screenHeight / 2;
        birdWidth = (int) resources.getDimension(R.dimen.bird_width);
        birdHeight = (int) resources.getDimension(R.dimen.bird_height);
        gravity = resources.getDimension(R.dimen.gravity); //instantiate gravity
        flappyBoost = resources.getDimension(R.dimen.flappyBoost);


        //Sprite with a bitmap with the size of the image file that we have as bird_down
        Bitmap birdBmpDown = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
        //To scale:
        bird_down = Bitmap.createScaledBitmap(birdBmpDown, birdWidth, birdHeight, false);
        Bitmap birdBmpUp = BitmapFactory.decodeResource(resources, R.drawable.bird_up);
        bird_up = Bitmap.createScaledBitmap(birdBmpUp, birdWidth, birdHeight, false);
    }

    @Override
    public void draw(Canvas canvas) {
        //To make sprites interchangeable
        if (currentFallingSpeed < 0) {
            //update y pos with speed
            canvas.drawBitmap(bird_up, birdX, birdY, null);     //negative speed, so up
        } else {
            canvas.drawBitmap(bird_down, birdX, birdY, null);
        }

    }

    @Override
    public void update() {
        if (collision) {
            //if bird on screen
            if (birdY + bird_down.getHeight() < screenHeight){
                birdY += currentFallingSpeed;
                currentFallingSpeed += gravity;

            }
        } else {
            birdY += currentFallingSpeed;
            currentFallingSpeed += gravity;//gravity always add to current falling speed
            Rect birdPosition = new Rect(birdX, birdY, birdX+birdWidth, birdY + birdHeight);
            callback.updatePosition(birdPosition);
        }
    }

    public void onTouchEvent() {
        if (!collision) {
            currentFallingSpeed = flappyBoost;
        }
    }
    public void collision() {
        collision = true;
    }
}

