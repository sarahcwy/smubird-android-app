package com.example.cs205_smu_bird_app.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class ParticleExplosion implements sprite {

    protected class Particle
    {
        float currentLife;
        float lifeTime;
        float x_postion;
        float y_postion;
        float x_velocity;
        float y_velocity;
        int red;
        int blue;
        int green;
        int alpha;
        boolean active = true;

        public Particle(float x, float y, float maxLifeTime, float x_velocity, float y_velocity, int r, int g, int b)
        {
            this.x_postion = x;
            this.y_postion = y;
            this.lifeTime = maxLifeTime;
            this.currentLife = maxLifeTime;
            this.x_velocity = x_velocity;
            this.y_velocity = y_velocity;
            this.red = r;
            this.blue = b;
            this.green = g;
            this.alpha = 255;
        }
    }

    protected float timePassed = 0.f;
    protected float x_spawnpostion;
    protected float y_spawnpostion;
    protected ArrayList<Particle> particles;
    protected int numberOfParticles;
    protected Paint paint;

    long previousFrameTime;

    public ParticleExplosion(float x, float y, int numParticles)
    {
        paint = new Paint();
        x_spawnpostion = x;
        y_spawnpostion = y;

        numberOfParticles = numParticles;
        particles = new ArrayList<>();

        float initialMaxVelocity = 300.0f;
        Random random = new Random();
        previousFrameTime = System.nanoTime();

        for (int i = 0; i < numberOfParticles; i++)
        {
            int[] rgb = {200, 100, 40};
            rgb[0] += random.nextInt(50) - 25;
            rgb[1] += random.nextInt(100) - 50;
            rgb[2] += random.nextInt(40) - 50;

            float direction = (float) (random.nextFloat() * 2.0f * Math.PI);

            float random_x_velocity =  random.nextFloat() * -0.5f * initialMaxVelocity;
            float random_y_velocity =  random.nextFloat() * 0.5f * initialMaxVelocity;
            //float random_y_velocity =  20.f;
            particles.add(new Particle(x_spawnpostion + 50, y_spawnpostion + 50, RandomInt(2), random_x_velocity, random_y_velocity, rgb[0], rgb[1], rgb[2]));
        }

    }

    @Override
    public void draw(Canvas canvas) {
        for(Particle p: particles)
        {
            if(p.active)
            {
                paint.setColor(Color.argb(p.alpha, p.red, p.green, p.blue));
                canvas.drawCircle(p.x_postion, p.y_postion, RandomInt(25), paint);
            }
        }
    }

    @Override
    public void update() {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - previousFrameTime) / 1000000000.f; // convert nanoseconds to secs
        previousFrameTime = currentTime;
        timePassed += deltaTime;

        for(Particle p: particles)
        {
            if(p.active)
            {
                p.x_postion += p.x_velocity;
                p.y_postion += p.y_velocity;
                p.currentLife -= deltaTime;
                p.alpha = (int) ((p.currentLife / p.lifeTime) * 255);


                if(p.currentLife <= 0)
                {
                    p.active = false;
                    p.alpha = 0;
                    //particles.remove((p));
                }
            }
        }
    }

    public int RandomInt(int number) {
        Random random = new Random();

        return random.nextInt(number);
    }
}
