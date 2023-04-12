package com.example.cs205_smu_bird_app.sprites;

import android.graphics.Rect;

import java.util.ArrayList;

public interface BombCallback {
    void bombOffScreen(Bomb bomb);
    void updatePosition(Bomb bomb, ArrayList<Rect> positions);
}
