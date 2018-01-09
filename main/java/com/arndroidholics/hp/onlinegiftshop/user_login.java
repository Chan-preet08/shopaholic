package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class user_login extends AppCompatActivity
{
    EditText et1,et2;
    Button bt1;
    String u,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        bt1=(Button)findViewById(R.id.bt1);
        getSupportActionBar().hide();
    }
 public void go(View v)
 {
     Thread t1= new Thread(new Job1());
     t1.start();
 }
 public class Job1 implements Runnable
    {
     public void run()
     {
        try
        {
            //We will send text data along with query string in url
            //AND send file in the form of bytes after header

            u=et1.getText().toString();
            p=et2.getText().toString();

            u=u.trim();

            //convert spaces to %20
            u=URLEncoder.encode(u,"utf-8");
            p=URLEncoder.encode(p,"utf-8");
            final String urlpath=MyClient.SERVERPATH+"login_check?un="+u+"&ps="+p;

            URL url = new URL(urlpath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int resCode = connection.getResponseCode();
            Log.d("MYMSG",resCode+"      res code");
            if (resCode == HttpURLConnection.HTTP_OK)
            {
                //for incoming data used buffer
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                while (true)
                {
                    String s = br.readLine();

                    if (s == null)
                    {
                        break;
                    }
                    sb.append(s);
                }

                //This time its simple String Data, But it can be JSON
                final String ans=sb.toString();
                Log.d("MYMESSAGE","ANSWER FROM SERVER "+ans);
                 if(ans.equals("success"))
                 {
                     MyClient.loggedinemail=u.trim();
                     Intent in = new Intent(user_login.this,user_home.class);
                     startActivity(in);
                 }
                //Now render data in GUI=
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),ans ,Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else if(resCode==HttpURLConnection.HTTP_NOT_FOUND)
            {
                Log.d("MYMESSAGE","404 NOT FOUND");
                Log.d("MYMESSAGE","urlpath "+urlpath);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"404 NOT FOUND\nCHECK Web Path\n"+urlpath,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        /// NW CODE ENDS HERE

     }
    }
    public void go3(View v)
    {
        Intent in = new Intent(getApplicationContext(),signup.class);
        startActivity(in);
    }
}
