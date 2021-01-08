package com.example.spaceshot.Gioco.GestoriCollisioni;

import android.graphics.Rect;
import android.util.Log;

import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.Gioco.GameView;
import com.example.spaceshot.Gioco.Object.Asteroide;
import com.example.spaceshot.Gioco.Player;
import com.example.spaceshot.Gioco.Shot;

public class GestioneCollisioniProiettili implements Runnable{

    public static boolean loop = true;

    @Override
    public void run()
    {
        while (GameActivity.CONTINUE)
        {
            if(GameActivity.PAUSE == false)
            {
                try { GameActivity.accesso_lista_proiettili.acquire(); } catch (InterruptedException e) { }
                try { GameActivity.accesso_lista_Asteroidi.acquire(); } catch (InterruptedException e) { }
                for(Shot s: GameView.lstShot)
                    for(Asteroide a:GameView.lstAsteroidi)
                        if(Rect.intersects(s.getBounds(), a.getBounds()))
                        {
                            a.TakeDamage(Player.ShotDamage);
                            if(a.health <= 0)
                                a.hide();
                            s.hide();
                        }

                GameActivity.accesso_lista_Asteroidi.release();
                GameActivity.accesso_lista_proiettili.release();
            }
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }
    }
}
