package com.example.cs205_smu_bird_app.sprites;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cs205_smu_bird_app.GameManagerCallback;
import com.example.cs205_smu_bird_app.R;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class BombManager implements BombCallback{
    private int interval; //interval between 2 obs
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    private Resources resources;

    private int progress = 0; //know when to make new obstacle
    private int speed; //speed of obstacle
    private GameManagerCallback callback;

    private static final int MAX_BOMBS_ON_SCREEN = 5;

    public BombManager(Resources resources, int screenHeight, int screenWidth, GameManagerCallback callback) {
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.callback = callback;
        interval = (int) resources.getDimension(R.dimen.obstacle_interval);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        bombs.add(new Bomb(resources,screenHeight,screenWidth, this));
        interval = 400; // Distance between bombs


    }

    public void removeBomb(Bomb bomb) {
        // Implement the logic for removing the bomb from the list or array of bombs
        // For example, if you have an ArrayList of bombs:
        bombs.remove(bomb);
    }

    public void update() {
        if (bombs.size() < MAX_BOMBS_ON_SCREEN) {
            progress += speed;          //if fewer bombs than max on screen
            if (progress > interval) {
                progress = 0;
                // Randomly decide if a new bomb should be created
                Random random = new Random();
                if (random.nextInt(4) > 0) { // 75% chance of creating a new bomb
                    bombs.add(new Bomb(resources, screenHeight, screenWidth, this));
                }
            }
        }
        List<Bomb> duplicate = new ArrayList<>();
        duplicate.addAll(bombs);
        for (Bomb bomb : duplicate) {
            bomb.update();
        }
    }

    public void draw(Canvas canvas) {
        for (Bomb bombs:bombs) {
            bombs.draw(canvas);

        }
    }

    @Override
    public void bombOffScreen(Bomb bomb) {
        //use interface
        bombs.remove(bomb);         //IMPT: THIS CAN BE CALLED WHILE FOR LOOP IN UPDATE IS EXECUTING BECAUSE DIFF THREAD!
        callback.removeBomb(bomb);
    }

    @Override
    public void updatePosition(Bomb bomb, ArrayList<Rect> positions) {
        int xPosition = bomb.getX();
        int yPosition = bomb.getY();
        int width = bomb.getWidth();
        int height = bomb.getHeight();
        Rect bombPosition = new Rect(xPosition, yPosition, xPosition + width, yPosition + height);
        positions.add(bombPosition);
        callback.updateBombPosition(bomb, positions);
    }


}
