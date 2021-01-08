package com.example.spaceshot.Gioco.GestoriCollisioni;

import android.graphics.Rect;
import android.util.Log;

import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.Gioco.GameView;
import com.example.spaceshot.Gioco.Object.Asteroide;

public class GestioneCollisioniAsteroridi{

    public static void Gestisci(Asteroide a)
    {
            if(GameView.player.hittable &&  Rect.intersects(a.getBounds(), GameView.player.getBounds()))
            {
                Log.d("COLPITO", "COLPITO");
                GameView.player.Hitted();
                a.hide();
            }
    }
}
