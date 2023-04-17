package com.example.cs205_smu_bird_app;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class MainThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private GameManager gameManager;
    private boolean running;
    private static Canvas canvas;
    private int targetFPS = 60; //hardcoded Frame Rate
    //SurfaceHolder allow for manipulation of canvas
    //scoreCounter scoreCounter = new scoreCounter();
    public MainThread(SurfaceHolder surfaceHolder, GameManager gameManager){
        this.surfaceHolder = surfaceHolder;
        this.gameManager = gameManager;
    }

    public void setRunning(boolean isRunning) {

        running = isRunning;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long targetTime = 1000 / targetFPS;

        //put scoreThread here

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                //get ref to canvas
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameManager.update();
                    gameManager.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000; //since nanotime so 1 mil divided by
            //dont allow run more than 60FPS, just wait for next
            waitTime = targetTime - timeMillis;

            try {
                if(waitTime > 0 ) {
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
