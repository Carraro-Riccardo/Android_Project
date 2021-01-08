package com.example.spaceshot.Gioco.Object;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

import static com.example.spaceshot.Gioco.GameView.screenX;
import static com.example.spaceshot.Gioco.GameView.screenY;

public class Bonus {

    public int x = 0,y, width, height, speed = 20;
    Bitmap image;

    public BonusType ID;

    public Bonus(Bitmap image, BonusType bonusType)
    {
        this.ID = bonusType;
        this.image = image;

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
        return new Rect(x,y,x + width, y+height);
    }

    public void hide()
    {
        x = 0-width;
        y = 0-height;
    }
}
