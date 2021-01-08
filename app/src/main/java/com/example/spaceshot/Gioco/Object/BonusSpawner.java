package com.example.spaceshot.Gioco.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.Gioco.GameView;
import com.example.spaceshot.R;

import java.util.Random;

public class BonusSpawner implements Runnable{

    public static boolean loop = true;
    public int delay = 4000;
    public Context context;

    public BonusSpawner(Context context)
    {
        this.context = context;
    }

    @Override
    public void run()
    {
        while(GameActivity.CONTINUE)
        {
            if(GameActivity.PAUSE == false) {
                long millis = System.currentTimeMillis();

                try { Thread.sleep(delay); } catch (InterruptedException e) { }

                try {
                    GameActivity.accesso_lista_Bonus.acquire();
                } catch (InterruptedException e) {
                }

                GameView.lstBonus.add(NuovoBonus());

                GameActivity.accesso_lista_Bonus.release();
            }
        }
    }

    private Bonus NuovoBonus()
    {
        Bitmap image = null;
        BonusType bonus = null;

        switch(new Random().nextInt(3))
        {
            case 0:
                bonus = BonusType.Double;
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.double_points);
                break;

            case 1:
                bonus = BonusType.Invincible;
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield);
                break;

            case 2:
                bonus = BonusType.Slow;
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.slow_down);
                break;

            default:
                bonus = BonusType.Double;
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.double_points);
                break;
        }

        return new Bonus(image, bonus);
    }
}
