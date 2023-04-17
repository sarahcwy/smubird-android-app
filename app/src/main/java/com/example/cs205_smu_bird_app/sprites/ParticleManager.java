package com.example.cs205_smu_bird_app.sprites;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    private ArrayList<ParticleExplosion> particles = new ArrayList<>();

    public void update() {
        for (ParticleExplosion p:particles) {
            p.update();
        }
    }

    public void draw(Canvas canvas) {
        for (ParticleExplosion p:particles) {
            p.draw(canvas);
        }
    }

    public void addEffect(ParticleExplosion p)
    {
        particles.add(p);
    }
}
