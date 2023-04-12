package com.example.cs205_smu_bird_app.sprites;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cs205_smu_bird_app.GameManagerCallback;
import com.example.cs205_smu_bird_app.R;

import java.util.ArrayList;
import java.util.List;

public class BombManager implements BombCallback{
    private int interval; //interval between 2 obs
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    private Resources resources;

    private int progress = 0; //know when to make new obstacle
    private int speed; //speed of obstacle
    private GameManagerCallback callback;

    public BombManager(Resources resources, int screenHeight, int screenWidth, GameManagerCallback callback) {
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.callback = callback;
        interval = (int) resources.getDimension(R.dimen.obstacle_interval);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        bombs.add(new Bomb(resources,screenHeight,screenWidth, this));

    }

    public void update() {
        progress += speed;
        if (progress > interval) {
            progress = 0;
            bombs.add(new Bomb(resources,screenHeight,screenWidth,this));
        }
        List<Bomb> duplicate = new ArrayList<>();
        duplicate.addAll(bombs);
        for (Bomb bomb: duplicate) {
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
        callback.updateBombPosition(bomb, positions);
    }
}
