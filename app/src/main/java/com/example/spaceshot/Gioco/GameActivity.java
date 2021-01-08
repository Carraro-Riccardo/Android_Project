package com.example.spaceshot.Gioco;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spaceshot.R;
import com.example.spaceshot.SampleFragment;

import java.util.concurrent.Semaphore;

import static com.example.spaceshot.Gioco.GameView.player;
import static com.example.spaceshot.Gioco.GameView.screenRatioX;
import static com.example.spaceshot.Gioco.GameView.screenRatioY;
import static com.example.spaceshot.Gioco.GameView.screenX;
import static com.example.spaceshot.Gioco.GameView.screenY;

public class GameActivity extends AppCompatActivity {

    public static   GameView gameView;
    static public Semaphore accesso_lista_proiettili = new Semaphore(1);
    static public Semaphore accesso_lista_Asteroidi = new Semaphore(1);
    static public Semaphore accesso_lista_Bonus = new Semaphore(1);

    Button b;

    public static int ID = -1;
    public static TextView txtPunteggio = null;
    public static boolean PAUSE = false;
    public static boolean CONTINUE = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ID = getIntent().getExtras().getInt("ID");
        Log.d("ID", ID +"");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        FrameLayout game = new FrameLayout(this);
        game.setId(View.generateViewId());
        gameView = new GameView(this,point.x, point.y );
        RelativeLayout UI_layout = new RelativeLayout(this);
        UI_layout.setId(View.generateViewId());
        UI_layout.setX(0);
        UI_layout.setY(0);

        ImageButton shootingButton = new ImageButton(this);
        Bitmap immaginePulsante = BitmapFactory.decodeResource(getResources(), R.drawable.palla);
        int width = (int) (immaginePulsante.getWidth() * screenRatioX / 5);
        int height = (int) (immaginePulsante.getHeight() * screenRatioY / 5);
        immaginePulsante = Bitmap.createScaledBitmap(immaginePulsante, width, height, false);
        shootingButton.setImageBitmap(immaginePulsante);
        shootingButton.setBackgroundDrawable(null);
        shootingButton.setX(1600);
        shootingButton.setY(750);
        shootingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.newShot();
            }
        });
        UI_layout.addView(shootingButton);

        ImageButton movementButton = new ImageButton(this);
        immaginePulsante = BitmapFactory.decodeResource(getResources(), R.drawable.palla);
        width = (int) (immaginePulsante.getWidth() * screenRatioX / 5);
        height = (int) (immaginePulsante.getHeight() * screenRatioY / 5);
        immaginePulsante = Bitmap.createScaledBitmap(immaginePulsante, width, height, false);
        movementButton.setImageBitmap(immaginePulsante);
        movementButton.setBackgroundDrawable(null);
        movementButton.setX(10);
        movementButton.setY(750);
        movementButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        player.isRising = false;
                        break;

                    case MotionEvent.ACTION_DOWN:
                        player.isRising = true;
                        break;
                }
                return true;
            }
        });
        UI_layout.addView(movementButton);

        txtPunteggio = new TextView(this);
        txtPunteggio.setWidth(300);
        txtPunteggio.setHeight(50);
        txtPunteggio.setX(screenX/2 - 150);
        txtPunteggio.setY(txtPunteggio.getHeight()/2);
        txtPunteggio.setText("0");
        txtPunteggio.setBackgroundColor(Color.WHITE);
        txtPunteggio.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        txtPunteggio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        UI_layout.addView(txtPunteggio);


        ImageButton pauseButton = new ImageButton(this);
        immaginePulsante = BitmapFactory.decodeResource(getResources(), R.drawable.pause_button);
        width = (int) (immaginePulsante.getWidth() * screenRatioX / 5) ;
        height = (int) (immaginePulsante.getHeight() * screenRatioY / 5);
        immaginePulsante = Bitmap.createScaledBitmap(immaginePulsante, width, height, false);
        pauseButton.setImageBitmap(immaginePulsante);
        pauseButton.setBackgroundDrawable(null);
        pauseButton.setX(0);
        pauseButton.setY(0);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAUSE = true;
                onPause();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft  = fm.beginTransaction();
                Fragment s = new SampleFragment();

                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(UI_layout.getId(), s);
                ft.commit();
            }
        });
        UI_layout.addView(pauseButton);

        game.addView(gameView);
        game.addView(UI_layout);
        setContentView(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    public static  void AVVIA()
    {
        CONTINUE = true;
        PAUSE = false;
    }

    public static void STOP()
    {
        CONTINUE = false;
        PAUSE = false;
    }
}