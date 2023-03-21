package com.example.cs205_smu_bird_app;

import android.graphics.Rect;

import com.example.cs205_smu_bird_app.sprites.Obstacle;

import java.util.ArrayList;

public interface GameManagerCallback {
    void updatePosition(Rect birdPosition);
    void updatePosition(Obstacle obstacle, ArrayList<Rect> positions);
    void removeObstacle(Obstacle obstacle);

}
