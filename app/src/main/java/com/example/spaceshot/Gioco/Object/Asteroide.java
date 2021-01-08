package com.example.spaceshot.Gioco.Object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.spaceshot.R;

import java.util.Random;

import androidx.constraintlayout.solver.widgets.Rectangle;

import static com.example.spaceshot.Gioco.GameView.screenRatioX;
import static com.example.spaceshot.Gioco.GameView.screenRatioY;
import static com.example.spaceshot.Gioco.GameView.screenX;
import static com.example.spaceshot.Gioco.GameView.screenY;

public class Asteroide {

    public int x = 0,y, width, height, speed = 20;
    Bitmap image;
    public int health;

    public Asteroide(Bitmap img, int health)
    {
        this.health = health;
        image = img;

        width = image.getWidth();
        height = image.getHeight();

       // width *= (int) screenRatioX;
        //height *= (int) screenRatioY;

        image = Bitmap.createScaledBitmap(image, width, height, false);

        x = screenX;
        y = new Random().nextInt(screenY - height);
    }

    public Bitmap getImage()
    {
        return image;
    }

    public Rect getBounds()
    {
        return new Rect(x + (int)(0.12*width),y + (int)(0.12*height),x + width - (int)(0.12*width), y+height - (int)(0.12*height));
    }

    public void hide()
    {
        x = 0-width;
        y = 0-height;
    }

    public void TakeDamage(int damage)
    {
        health -= damage;
    }
}
