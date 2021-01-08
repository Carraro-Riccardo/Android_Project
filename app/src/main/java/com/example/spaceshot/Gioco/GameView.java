package com.example.spaceshot.Gioco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.spaceshot.Gioco.GestoriCollisioni.GestioneCollisioniAsteroridi;
import com.example.spaceshot.Gioco.GestoriCollisioni.GestioneCollisioniPlayer;
import com.example.spaceshot.Gioco.GestoriCollisioni.GestioneCollisioniProiettili;
import com.example.spaceshot.Gioco.Object.Asteroide;
import com.example.spaceshot.Gioco.Object.AsteroidiSpawner;
import com.example.spaceshot.Gioco.Object.Bonus;
import com.example.spaceshot.Gioco.Object.BonusSpawner;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    public  static int GAME_DIFFICULTY = 500;

    private Thread thread;
    boolean isPlaying = true;
    private Background first, second;
    public static  float screenRatioX, screenRatioY;
    private Paint paint;
    public static int screenX, screenY;

    public static Player player;

    public static List<Shot> lstShot;
    public static List<Asteroide> lstAsteroidi;
    public static List<Bonus> lstBonus;


    public GameView(Context context, int screenX, int screenY)
    {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        first = new Background(screenX, screenY, getResources());
        second = new Background(screenX, screenY, getResources());

        player = new Player(this, screenY, getResources());
        lstShot = new ArrayList<Shot>();
        lstAsteroidi = new ArrayList<Asteroide>();
        lstBonus = new ArrayList<Bonus>();

        new Thread(new AsteroidiSpawner( GAME_DIFFICULTY, context)).start();
        new Thread(new BonusSpawner(context)).start();
        new Thread(new GestioneCollisioniProiettili()).start();
        new Thread(new CalcolatorePunteggio()).start();

        second.x = screenX;

        paint = new Paint();
    }

    public void resume()
    {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }


    public  void pause()
    {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        long tempo = System.currentTimeMillis();
        while(isPlaying)
        {
                Update();
                draw();
                sleep();
        }
    }

    public void Update()
    {
        int velocity = (int)(10 * screenRatioX);
        first.x -= velocity;
        second.x -=  velocity;

        if(first.x + first.background.getWidth() <= 0)
            first.x = screenX;

        if(second.x + second.background.getWidth() <= 0)
            second.x = screenX;

        if(player.isRising)
            player.y -= player.speed * screenRatioY;
        else
            player.y += player.speed * screenRatioY;

        if(player.y <= 0)
            player.y = 0;

        if(player.y >= screenY - player.height)
            player.y = screenY - player.height;


        //
        // PROIETTILI
        //
        List<Shot> ShotDaEliminare = new ArrayList<>();
        try {  GameActivity.accesso_lista_proiettili.acquire(); } catch (InterruptedException e) { }
        for(Shot s:lstShot)
        {
            if(s.x > screenX)
                ShotDaEliminare.add(s);

            else
                s.x += 50 * screenRatioX;
        }
        GameActivity.accesso_lista_proiettili.release();

        //
        // ASTEROIDI
        //
        List<Asteroide> AsteroidiDaEliminare = new ArrayList<Asteroide>();
        try {  GameActivity.accesso_lista_Asteroidi.acquire(); } catch (InterruptedException e) { }
        for(Asteroide a:lstAsteroidi)
        {
            if(a.x + a.width < 0)
                AsteroidiDaEliminare.add(a);

            else
            {
                GestioneCollisioniAsteroridi.Gestisci(a);

                a.x -= a.speed * screenRatioX;
            }

        }
        GameActivity.accesso_lista_Asteroidi.release();

        //
        // BONUS
        //
        List<Bonus> BonusDaEliminare = new ArrayList<Bonus>();
        try {  GameActivity.accesso_lista_Bonus.acquire(); } catch (InterruptedException e) { }
        for(Bonus b:lstBonus)
        {
            if(b.x + b.width < 0)
                BonusDaEliminare.add(b);

            else
            {
                GestioneCollisioniPlayer.Gestisci(b);

                b.x -= b.speed * screenRatioX;
            }

        }
        GameActivity.accesso_lista_Bonus.release();

        //
        //ELIMINAZIONE
        //
        try {  GameActivity.accesso_lista_proiettili.acquire(); } catch (InterruptedException e) { }
        for(Shot s: ShotDaEliminare)
            lstShot.remove(s);
        GameActivity.accesso_lista_proiettili.release();

        try {  GameActivity.accesso_lista_Asteroidi.acquire(); } catch (InterruptedException e) { }
        for(Asteroide a: AsteroidiDaEliminare)
            lstAsteroidi.remove(a);
        GameActivity.accesso_lista_Asteroidi.release();

        try {  GameActivity.accesso_lista_Bonus.acquire(); } catch (InterruptedException e) { }
        for(Bonus b: BonusDaEliminare)
            lstBonus.remove(b);
        GameActivity.accesso_lista_Bonus.release();
    }

    public void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(first.background, first.x, first.y, paint);
            canvas.drawBitmap(second.background, second.x, second.y, paint);

            canvas.drawBitmap(player.getImage(), player.x, player.y,paint );

            try { GameActivity.accesso_lista_proiettili.acquire(); } catch (InterruptedException e) { }
            for(Shot s:lstShot)
                canvas.drawBitmap(s.shotImage, s.x, s.y, paint);
            GameActivity.accesso_lista_proiettili.release();

            try { GameActivity.accesso_lista_Asteroidi.acquire(); } catch (InterruptedException e) { }
            for(Asteroide a:lstAsteroidi)
                canvas.drawBitmap(a.getImage(), a.x, a.y, paint);
            GameActivity.accesso_lista_Asteroidi.release();

            try { GameActivity.accesso_lista_Bonus.acquire(); } catch (InterruptedException e) { }
            for(Bonus b:lstBonus)
                canvas.drawBitmap(b.getImage(), b.x, b.y, paint);
            GameActivity.accesso_lista_Bonus.release();

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void sleep() {
        try { thread.sleep(15); }catch (Exception e){}
    }

    public void newShot() {

        Shot shot = new Shot(getResources());
        shot.x = player.x + player.width;
        shot.y = player.y + player.height/2;

        try { GameActivity.accesso_lista_proiettili.acquire(); } catch (InterruptedException e) {  }
        lstShot.add(shot);
        GameActivity.accesso_lista_proiettili.release();
    }
}
