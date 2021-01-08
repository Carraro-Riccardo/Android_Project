package com.example.spaceshot.Accesso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.R;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static int ID = -1;
    private String usernameSalvato = "";
    private String passwordSalvata = "";
    private boolean loggato = false;

    public static final String NAME_SAVED_DATA = "saved_data";

    //LOGIN VIEWS
    TextView txtUsername = null;
    TextView txtPassword = null;
    Button btnLogin = null;
    Button btnRegister = null;

    // AFTER LOGIN
    Button btnPlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        LoadLoginInformation();

        if (loggato == false) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        HttpGet get = new HttpGet("http://ec2-3-139-100-8.us-east-2.compute.amazonaws.com/MIO_SITO/Login.php?e=" +
                                usernameSalvato
                                +"&p="+
                                passwordSalvata
                        );

                        Log.d("PASSWORD", passwordSalvata);
                        Log.d("USERNAME", usernameSalvato);

                        HttpClient client = new DefaultHttpClient();
                        HttpResponse resource = client.execute(get);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        resource.getEntity().writeTo(out);
                        String data = out.toString();
                        out.close();

                        JSONArray jarray = new JSONArray(data);
                        JSONObject obj = jarray.getJSONObject(0);

                        ID = obj.getInt("id");
                        Log.d("ID_MAINACTIVITY", ID+"");
                    } catch (Exception e) {}
                }
            });

            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            btnPlay = (Button) findViewById(R.id.btnPlay);
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameActivity.AVVIA();
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);

                }
            });
            btnPlay.setBackgroundResource(R.drawable.btn_play);

            ImageView img = (ImageView) findViewById(R.id.imgFloat);
            img.setImageResource(R.drawable.astronave);
            img.setAnimation(AnimationUtils.loadAnimation(this, R.anim.floating_effect));
        }
    }

    private void LoadLoginInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_SAVED_DATA, MODE_PRIVATE);

        usernameSalvato = sharedPreferences.getString("username", "");
        passwordSalvata = sharedPreferences.getString("pass", "");
        loggato = sharedPreferences.getBoolean("loggato", false);
    }

}