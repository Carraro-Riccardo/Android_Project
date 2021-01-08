package com.example.spaceshot.Gioco;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.spaceshot.R;

import static android.view.View.generateViewId;
import static com.example.spaceshot.Gioco.GameView.screenRatioX;
import static com.example.spaceshot.Gioco.GameView.screenRatioY;
import static com.example.spaceshot.Gioco.GameView.screenX;
import static com.example.spaceshot.Gioco.GameView.screenY;

public class Shot {

    int x, y, width, height;
    Bitmap shotImage;

    Shot(/*int x, int y, */Resources res)
    {
        shotImage = BitmapFactory.decodeResource(res, R.drawable.space_shot);

         width = shotImage.getWidth();
         height = shotImage.getHeight();

        width *= screenRatioX;
        height *= screenRatioY;

        shotImage = Bitmap.createScaledBitmap(shotImage, width, height, false);

    }

    public Rect getBounds()
    {
        return new Rect(x, y, x+width, y+height);
    }

    public void hide()
    {
        x = screenX + width;
        y = screenY + height;
    }

}
