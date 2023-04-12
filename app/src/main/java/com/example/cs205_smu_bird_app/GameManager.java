package com.example.cs205_smu_bird_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.os.Vibrator;

import com.example.cs205_smu_bird_app.sprites.Bird;
import com.example.cs205_smu_bird_app.sprites.Background;
import com.example.cs205_smu_bird_app.sprites.GameMessage;
import com.example.cs205_smu_bird_app.sprites.GameOver;
import com.example.cs205_smu_bird_app.sprites.Obstacle;
import com.example.cs205_smu_bird_app.sprites.ObstacleManager;
import com.example.cs205_smu_bird_app.sprites.BombManager;
import com.example.cs205_smu_bird_app.sprites.Bomb;
import com.example.cs205_smu_bird_app.sprites.Score;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, GameManagerCallback {
    private static final String APP_NAME = "SMUBIRD";
    public MainThread thread;
    private GameState gameState = GameState.INITIAL;

    private Bird bird;
    private GameMessage gameMessage;
    private Score scoreSprite;
    private int score;
    private Background background;
    private DisplayMetrics dm;
    private GameOver gameOver;
    private ObstacleManager obstacleManager;
    private BombManager bombManager;
    private Rect birdPosition;
    private HashMap<Obstacle, List<Rect>> obstaclePositions = new HashMap<>();
    private HashMap<Bomb, List<Rect>> bombPositions = new HashMap<>();
    private MediaPlayer mpPoint;
    private MediaPlayer mpSwoosh;
    private MediaPlayer mpHit;
    private MediaPlayer mpWing;
    private MediaPlayer mpDieTest;
    private MediaPlayer mpDie;

    Vibrator vibrator = getContext().getSystemService(Vibrator.class);

    public GameManager(Context context, AttributeSet attributeSet) {
        super(context);
        initSounds();
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        initGame();
    }

    private void initGame() {
        score = 0;
        birdPosition = new Rect();
        obstaclePositions = new HashMap<>();
        bombPositions = new HashMap<>();
        //bird creation! by giving it resources to make a bird
        //now call the bird in draw method!
        bird = new Bird(getResources(), dm.heightPixels, this);
        background = new Background(getResources(), dm.heightPixels);
        obstacleManager = new ObstacleManager(getResources(), dm.heightPixels, dm.widthPixels, this);
        bombManager = new BombManager(getResources(), dm.heightPixels, dm.widthPixels, this);
        gameOver = new GameOver(getResources(), dm.heightPixels, dm.widthPixels);
        gameMessage = new GameMessage(getResources(), dm.heightPixels, dm.widthPixels);
        scoreSprite = new Score(getResources(), dm.heightPixels, dm.widthPixels);

    }

    private void initSounds() {
        mpPoint = MediaPlayer.create(getContext(), R.raw.point);
        mpSwoosh = MediaPlayer.create(getContext(), R.raw.swoosh);
        mpDie = MediaPlayer.create(getContext(), R.raw.die);
        mpHit = MediaPlayer.create(getContext(), R.raw.hit);
        mpWing = MediaPlayer.create(getContext(), R.raw.wing);
        mpDieTest = MediaPlayer.create(getContext(), R.raw.dietest);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new MainThread(holder, this);
            //Prevents crashing when I exit to home screen and go back to app since the thread will stop!
            //Resets thread at the back of the game
        }
        //add method to know when thread is started & running
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //when no longer update UI, stop background from running
        //might get exception since thread

        //so try stop, and keep trying since no thread can keep running in BG after finished game
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //Collision logic is that bird,obstacle etc will callbackto update pos to gameManager
    //GM will then calculate collision and do handling
    public void update() {
        switch(gameState) {
            case PLAYING:
                //place bird sprite somewhere on screen
                bird.update();
                obstacleManager.update();
                bombManager.update();
                //System.out.println("GameManager update call");
                break;
            case GAME_OVER:
                bird.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawRGB(150, 255, 255);
            background.draw(canvas); //put this first so that bird is IN FRONT
            switch(gameState) {
                case PLAYING:
                    bird.draw(canvas); //bird calling
                    obstacleManager.draw(canvas);
                    bombManager.draw(canvas);
                    scoreSprite.draw(canvas);
                    calculateCollision();       //occurs everytime new bird, new pos to ensure alw collision checked
                    break;

                case INITIAL:
                    bird.draw(canvas);
                    gameMessage.draw(canvas);
                    break;

                case GAME_OVER:
                    bird.draw(canvas);
                    obstacleManager.draw(canvas);
                    bombManager.draw(canvas);
                    gameOver.draw(canvas);
                    scoreSprite.draw(canvas);
                    break;
            }

        }
    }

    //On press
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case PLAYING:
                bird.onTouchEvent();
                mpWing.start();
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)); // Vibrate for 100 milliseconds

                break;

            case INITIAL:
                initGame();
                bird.onTouchEvent();//make bird fly not drop like potato @ start
                mpWing.start();
                gameState = GameState.PLAYING;
                mpSwoosh.start();//play start sound!
                break;

            case GAME_OVER:
                initGame();
                gameState = GameState.INITIAL; //reset to start
                break;

        }
        //bird.onTouchEvent();
        return super.onTouchEvent(event);
    }


    @Override
    public void updatePosition(Rect birdPosition) {
        this.birdPosition = birdPosition;
    }

    @Override
    public void updatePosition(Obstacle obstacle, ArrayList<Rect> positions) {
        //update each obstacle everytime
        //likely each obstacle is alr in map
        if (obstaclePositions.containsKey(obstacle)) {
            obstaclePositions.remove(obstacle);
        }
        obstaclePositions.put(obstacle,positions);
    }

    @Override
    public void removeObstacle(Obstacle obstacle) {
        obstaclePositions.remove(obstacle);
        score++;
        scoreSprite.updateScore(score);
        mpPoint.start();
    }

    @Override
    public void updateBombPosition(Bomb bomb, ArrayList<Rect> positions) {
        if (bombPositions.containsKey(bomb)) {
            bombPositions.remove(bomb);
        }
        bombPositions.put(bomb,positions);
    }

    @Override
    public void removeBomb(Bomb bomb) {
        bombPositions.remove(bomb);
        score++;
        scoreSprite.updateScore(score);
        mpDie.start();
    }

    //calculate collision occured or not
    public void calculateCollision() {
        boolean collision = false;
        if (birdPosition.bottom > dm.heightPixels) {
            collision = true;
        } else {
            for (Obstacle obstacle: obstaclePositions.keySet()) {
                Rect bottomRectangle = obstaclePositions.get(obstacle).get(0);
                Rect topRectangle = obstaclePositions.get(obstacle).get(1);
                if (birdPosition.right > bottomRectangle.left && birdPosition.left < bottomRectangle.right && birdPosition.bottom > bottomRectangle.top){
                    collision = true;
                } else if (birdPosition.right > topRectangle.left && birdPosition.left < topRectangle.right && birdPosition.top < topRectangle.bottom) {
                    collision = true;
                }
                //collision checked
            }
        }
        if (collision) {
            //implement Game Over Here!
            gameState = GameState.GAME_OVER;
            bird.collision();
            scoreSprite.collision(getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));            //let SS know collision
            mpHit.start(); //play hit!
            mpHit.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mpDieTest.start();
                }
            });
        }
    }

}
