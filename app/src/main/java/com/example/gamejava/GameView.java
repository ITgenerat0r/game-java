package com.example.gamejava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = -4;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Wall[] walls;
    private SharedPreferences preferences;
    private Random random;
    private Flight flight;
    private GameActivity activity;
    private Background background1, background2;
    private int speed = 9, speed_turn = 5;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        preferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1920f / screenY;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        flight = new Flight(screenX, screenY, getResources());

        background2.y = 0 - background2.background.getHeight();

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);


        walls = new Wall[4];

        for (int i = 0; i < 4; i++){
            Wall wall = new Wall(getResources(), screenY);
            walls[i] = wall;
        }

        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying){

            update();
            draw();
            sleep();
        }
    }

    private void update(){

        background1.y += speed * screenRatioY / screenRatioY;
        background2.y += speed * screenRatioY / screenRatioY;

        if(background1.y > screenY){
            background1.y = 0 - background1.background.getHeight();
        }

        if(background2.y > screenY){
            background2.y = 0 - background2.background.getHeight();
        }

        if (flight.isGoingLeft){
            flight.x -= speed_turn * screenRatioX;
        }

        if (flight.isGoingRight)
            flight.x += speed_turn * screenRatioX;

        if (flight.x < 0)
            flight.x = 0;

        if (flight.x > screenX - flight.width)
            flight.x = screenX - flight.width;

        for (Wall wall: walls){

            wall.y += wall.speed;

            if (wall.y < 0 - wall.height){
                wall.y = 0 - wall.height - random.nextInt(screenY-wall.height);
            }

            if (wall.y > screenY){

                score++;
                //int bound = (int) (30 * screenRatioY);
                //wall.speed = random.nextInt(bound);

                wall.speed = speed;

                //if (wall.speed < 10 * screenRatioY){
                //    wall.speed = (int) (10 * screenRatioY);
                //}

                wall.y = 0 - wall.height - random.nextInt(screenY - wall.height);
                wall.x = random.nextInt(screenX - wall.width);

            }

            if (Rect.intersects(wall.getCollisionShip(), flight.getCollisionShip())){

                isGameOver = true;
                return;
            }

        }

    }

    private void draw(){

        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);

            for (Wall wall: walls){
                canvas.drawBitmap(wall.getWall(), wall.x, wall.y, paint);
            }

            canvas.drawText(score + "", screenX/2f, 64, paint);

            if (isGameOver){
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(flight.getFlight(flight.isGoingLeft, flight.isGoingRight),flight.x, flight.y, paint);


            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(1000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (preferences.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep(){
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getX() < screenX / 2){
            flight.isGoingRight = false;
            flight.isGoingLeft = true;

        }else{
            flight.isGoingLeft = false;
            flight.isGoingRight = true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2){
                    flight.isGoingRight = false;
                    flight.isGoingLeft = true;

                }else{
                    flight.isGoingLeft = false;
                    flight.isGoingRight = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingLeft = false;
                flight.isGoingRight = false;
                break;
        }

        return true;
    }
}
