package com.example.spaceshot.Gioco;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.spaceshot.R;

import java.util.concurrent.Semaphore;

public class Player {

    static int default_speed = 30;
    int x,y, width, height, counter = 0,speed = default_speed;
    GameView gameView;
    boolean isRising = false;

    Bitmap hit, hit2;
    Bitmap shot;
    public static int ShotDamage = 50;
    public boolean hittable = true;

    Player(GameView gameView ,int screenY, Resources res)
    {
        this.gameView = gameView;
        hit  = BitmapFactory.decodeResource(res, R.drawable.astronave);
        hit2 = BitmapFactory.decodeResource(res, R.drawable.hit2);
        shot = BitmapFactory.decodeResource(res, R.drawable.space_shot);

        width  = hit.getWidth();
        height = hit.getHeight();

        //width /= 4;
        //height /= 4;

        width  *= GameView.screenRatioX * 1.5;
        height *= GameView.screenRatioY * 1.5;

        shot = Bitmap.createScaledBitmap(shot, width, height, false);
        hit  = Bitmap.createScaledBitmap(hit, width, height, false);
        hit2 = Bitmap.createScaledBitmap(hit2, width, height, false);

        y = screenY / 2;
        x = (int) (64 * GameView.screenRatioX);
    }

    Bitmap getImage()
    {
        if(counter == 0)
            return hit;

        else return hit2;
    }

    public void Spara() {
        gameView.newShot();
    }

    public Rect getBounds()
    {
        return new Rect(x  + (int)(0.12*width), y + (int)(0.12*height), x + width - (int)(0.12*width), y + height - (int)(0.12*height));
    }

    public void Hitted()
    {
        //fare peredere vita
        StartInvincible();
    }

    public void StartInvincible()
    {
        hittable = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                int durata_invincibilita = 3000;
                long currentMillis = 0;
                int delta_time = 500;
                boolean changeImg = true;

                while(durata_invincibilita > 0)
                {
                    if(GameActivity.PAUSE == false) {
                        if (System.currentTimeMillis() - currentMillis > delta_time) {
                            if (changeImg) counter = 0;
                            else counter = 1;

                            changeImg = !changeImg;

                            durata_invincibilita -= 200;
                            currentMillis = System.currentTimeMillis();
                        }
                    }
                }

                hittable = true;
            }
        }).start();
    }

    public void SlowDown()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int durata_effetto = 3000;
                long currentMillis = 0;
                int delta_time = 500;
                boolean changeImg = true;

                speed = default_speed/3;

                while(durata_effetto > 0)
                {
                    if(GameActivity.PAUSE == false) {
                        if (System.currentTimeMillis() - currentMillis > delta_time) {

                            durata_effetto -= delta_time;
                            currentMillis = System.currentTimeMillis();
                        }
                    }
                }

                speed = default_speed;
            }
        }).start();
    }
}
