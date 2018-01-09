package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class signup extends AppCompatActivity {
    EditText et1, et2, et3, et4, et5;
    Button bt1;
    String name, password, email, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        bt1 = (Button) findViewById(R.id.bt1);
        getSupportActionBar().hide();

    }

    public void go(View v) {
        Thread t1 = new Thread(new Job1());
        t1.start();
    }

    public class Job1 implements Runnable {
        public void run() {
            try {

                name = et1.getText().toString();
                email = et2.getText().toString();
                password = et4.getText().toString();
                number = et5.getText().toString();

                //convert spaces to %20
                name = URLEncoder.encode(name, "utf-8");
                password = URLEncoder.encode(password, "utf-8");
                email = URLEncoder.encode(email, "utf-8");
                number = URLEncoder.encode(number, "utf-8");

                final String urlpath = MyClient.SERVERPATH + "signup_entry?name=" + name + "&password=" + password + "&email=" + email + "&number=" + number;
                Log.d("chan",urlpath);
                URL url = new URL(urlpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int resCode = connection.getResponseCode();
                Log.d("MYMSG", resCode + "      res code");
                if (resCode == HttpURLConnection.HTTP_OK) {
                    //for incoming data used buffer
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    while (true) {
                        String s = br.readLine();

                        if (s == null) {
                            break;
                        }
                        sb.append(s);
                    }

                    //This time its simple String Data, But it can be JSON
                    final String ans = sb.toString();
                    Log.d("MYMESSAGE", "ANSWER FROM SERVER "+ans);
                    if (ans.equals("Signup Successful")) {
                        //Now render data in GUI=
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    if (ans.equals("User Already Exists")) {
                        //Now render data in GUI=
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else if (resCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d("MYMESSAGE", "404 NOT FOUND");
                    Log.d("MYMESSAGE", "urlpath " + urlpath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "404 NOT FOUND\nCHECK Web Path\n" + urlpath, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            /// NW CODE ENDS HERE

        }
    }
}



