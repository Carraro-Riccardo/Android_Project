package com.example.spaceshot.Accesso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.spaceshot.Gioco.GameActivity;
import com.example.spaceshot.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    //LOGIN VIEWS
    TextView txtUsername = null;
    TextView txtPassword = null;
    Button btnLogin = null;
    Button btnRegister = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_login);

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegistrazione);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            HttpGet get = new HttpGet("http://ec2-3-139-100-8.us-east-2.compute.amazonaws.com/MIO_SITO/Login.php?e=" +
                                    txtUsername.getText().toString().replace(" ","+")
                                    +"&p="+
                                    txtPassword.getText().toString().replace(" ", "+")
                            );
                            HttpClient client = new DefaultHttpClient();
                            HttpResponse resource = client.execute(get);

                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            resource.getEntity().writeTo(out);
                            String data = out.toString();
                            out.close();

                            JSONArray jarray = new JSONArray(data);
                            JSONObject obj = jarray.getJSONObject(0);

                            int id = obj.getInt("id");

                            SaveLoginInformation();

                            Intent intent = new Intent(Login.this, GameActivity.class);
                            intent.putExtra("ID",id);
                            startActivity(intent);

                        } catch (Exception e) {}
                    }
                });

                t.start();

                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void SaveLoginInformation()
    {
        String user = txtUsername.getText().toString().replace(" ", "+");
        String pass = txtPassword.getText().toString().replace(" ", "+");

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.NAME_SAVED_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", user);
        editor.putString("pass", pass);
        editor.putBoolean("loggato", true);

        editor.apply();
    }
}
