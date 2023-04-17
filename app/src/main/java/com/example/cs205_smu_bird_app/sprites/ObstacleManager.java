package com.example.cs205_smu_bird_app.sprites;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cs205_smu_bird_app.GameManagerCallback;
import com.example.cs205_smu_bird_app.R;

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager implements ObstacleCallback{

    private int interval; //interval between 2 obs
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    private Resources resources;

    private int progress = 0; //know when to make new obstacle
    private int speed; //speed of obstacle
    private GameManagerCallback callback;

    public ObstacleManager(Resources resources, int screenHeight, int screenWidth, GameManagerCallback callback) {
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.callback = callback;
        interval = (int) resources.getDimension(R.dimen.obstacle_interval);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        obstacles.add(new Obstacle(resources,screenHeight,screenWidth, this));
    }

    public void update() {
        progress += speed;
        if (progress > interval) {
            progress = 0;
            obstacles.add(new Obstacle(resources,screenHeight,screenWidth,this));
        }
        List<Obstacle> duplicate = new ArrayList<>();
        duplicate.addAll(obstacles);
        for (Obstacle obstacle: duplicate) {
            obstacle.update();
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle obstacles:obstacles) {
            obstacles.draw(canvas);
        }
    }

    @Override
    public void obstacleOffScreen(Obstacle obstacle) {
        //need way for obstacle to call into obstacle manager
        //use interface
        obstacles.remove(obstacle);         //IMPT: THIS CAN BE CALLED WHILE FOR LOOP IN UPDATE IS EXECUTING BECAUSE DIFF THREAD!
        callback.removeObstacle(obstacle);      //informs gameManager to remove obstacle from the game
    }

    @Override
    public void updatePosition(Obstacle obstacle, ArrayList<Rect> positions) {
        callback.updatePosition(obstacle, positions);       //informs game manager to update obstacle position in game
    }
}
