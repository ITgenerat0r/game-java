package com.example.gamejava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.gamejava.GameView.screenRatioX;
import static com.example.gamejava.GameView.screenRatioY;

public class Wall {

    public int speed = 10;
    int x, y, width, height, wallCounter = 1;

    Bitmap wall1, wall2;

    Wall (Resources res, int Y){

        wall1 = BitmapFactory.decodeResource(res, R.drawable.wall1);
        wall2 = BitmapFactory.decodeResource(res, R.drawable.wall2);

        width = wall1.getWidth();
        height = wall1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        wall1 = Bitmap.createScaledBitmap(wall1, width, height, false);
        wall2 = Bitmap.createScaledBitmap(wall2, width, height, false);

        y = Y;
        x = -height;

    }

    Bitmap getWall(){

        wallCounter++;

        if(wallCounter < 14){
            return wall1;
        }

        if(wallCounter > 22){
            wallCounter = 0;
        }


        return wall2;

    }

    Rect getCollisionShip(){
        return new Rect(x, y, x + width, y + height);
    }

}
