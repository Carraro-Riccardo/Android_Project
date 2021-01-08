package com.example.spaceshot.Gioco.GestoriCollisioni;

import android.graphics.Rect;
import android.util.Log;

import com.example.spaceshot.Gioco.CalcolatorePunteggio;
import com.example.spaceshot.Gioco.GameView;
import com.example.spaceshot.Gioco.Object.Bonus;
import com.example.spaceshot.Gioco.Object.BonusType;
import com.example.spaceshot.Gioco.Player;

public class GestioneCollisioniPlayer {

    public static void Gestisci(Bonus b)
    {
        if(Rect.intersects(b.getBounds(), GameView.player.getBounds()))
        {
            Log.d("BONUS", "BONUS");
            b.hide();
            ApplyEffect(GameView.player, b.ID);
        }
    }

    public static void ApplyEffect(Player player, BonusType ID)
    {
        switch (ID)
        {
            case Double:
                CalcolatorePunteggio.AvviaPunteggioDoppio();
                break;

            case Invincible:
                player.StartInvincible();
                break;

            case Slow:
                player.SlowDown();
                break;

            default:
                break;
        }
    }

}
