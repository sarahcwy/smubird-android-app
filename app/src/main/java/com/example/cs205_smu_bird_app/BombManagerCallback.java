package com.example.cs205_smu_bird_app;
import android.graphics.Rect;
import com.example.cs205_smu_bird_app.sprites.Bomb;

import java.util.ArrayList;

public interface BombManagerCallback {
    //void updatePosition(Rect birdPosition);
    void updatePosition(Bomb bomb, ArrayList<Rect> positions);
    void removeBomb(Bomb bomb);
}
