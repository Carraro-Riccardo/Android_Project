package com.example.spaceshot.Gioco.Object;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.Gioco.GameView;
import com.example.spaceshot.R;

import java.util.Random;

public class AsteroidiSpawner implements Runnable{

    Context context;
    int delay = 0;
    static boolean loop;


    public AsteroidiSpawner(int delay, Context context)
    {
        this.delay = delay;
        this.context = context;
        loop = true;
    }

    @Override
    public void run()
    {
        long millis = System.currentTimeMillis();

        while (GameActivity.CONTINUE)
        {
            if(GameActivity.PAUSE == false)
            {

                try { Thread.sleep(500); } catch (InterruptedException e) { }

                try {  GameActivity.accesso_lista_Asteroidi.acquire(); } catch (InterruptedException e) { }

                GameView.lstAsteroidi.add(NuovoAsteroide());

                GameActivity.accesso_lista_Asteroidi.release();

                millis = System.currentTimeMillis();
            }
        }

    }

    private Asteroide NuovoAsteroide()
    {
        int random = new Random().nextInt(3);

        Bitmap image = null;
        int life;

        switch (random)
        {
            case 0:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroide_small);
                life = 100;
                break;
            case 1:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroide_medium);
                life = 200;
                break;
            case 2:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroide_big);
                life = 300;
                break;
            default:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroide_small);
                life = 100;
                break;

        }


        return new Asteroide(image, life);
    }


}
