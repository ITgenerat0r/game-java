package com.example.gamejava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import static com.example.gamejava.GameView.screenRatioX;
import static com.example.gamejava.GameView.screenRatioY;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Flight {

    boolean isGoingRight = false;
    boolean isGoingLeft = false;
    int x, y, width, height, wingCounter = 0;
    Bitmap flight1, flight2, flight3, dead;

    Flight(int screenX, int screenY, Resources res){

        flight1 = BitmapFactory.decodeResource(res, R.drawable.car0);

        flight1 = BitmapFactory.decodeResource(res, R.drawable.body_car);
        flight2 = RotateBitmap(flight1, 20);
        flight3 = RotateBitmap(flight1, -20);


        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        Log.d("D", "ScreenRatioX = " + screenRatioX);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);
        flight3 = Bitmap.createScaledBitmap(flight3, width, height, false);


        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        x = screenX / 2;
        y = (int)(screenY - flight1.getHeight() * 2 * screenRatioY);

    }

    Bitmap getFlight (boolean L, boolean R){

        if (L){
            return flight3;
        }

        if (R)
            return flight2;

        return flight1;
    }

    Rect getCollisionShip(){
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead(){
        return dead;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Log.d("MainLog", "RotateBitmap()");
        int w, h;
        double wr, hr;
        float abs_angle = abs(angle);
        w = source.getWidth();
        h = source.getHeight();
        Log.d("MainLog", String.format("w = %d, h = %d", w, h));
        hr = h * cos(angle) + w * sin(angle);
        wr = h * sin(angle) + w * cos(angle);
//        w = (int) wr;
//        h = (int) hr;
        w = (int) (w * 1);
        h = (int) (h * 1);
        Log.d("MainLog", String.format("wr = %d, hr = %d", w, h));
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        source = Bitmap.createBitmap(source, 0, 0, (int) w, (int) h, matrix, true);
        return Bitmap.createScaledBitmap(source, w, h, false);
    }

}

