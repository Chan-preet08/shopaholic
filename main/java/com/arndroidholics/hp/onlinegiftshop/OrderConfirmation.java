package com.arndroidholics.hp.onlinegiftshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class OrderConfirmation extends AppCompatActivity
{
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    String booking_date,delivery_date,address_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        tv7=(TextView)findViewById(R.id.tv7);
        tv8=(TextView)findViewById(R.id.tv8);
        tv9=(TextView)findViewById(R.id.tv9);
        tv10=(TextView)findViewById(R.id.tv10);

        getSupportActionBar().hide();


        try
        {

            JSONArray jsonarr = new JSONArray();

            for(int i=0;i<MyClient.alglob.size();i++)
                          {
                              CartItem ci = MyClient.alglob.get(i);

                              JSONObject singleobject=new JSONObject();
                              singleobject.put("p_id", ci.p_id);
                              singleobject.put("p_name",ci.p_name);
                              singleobject.put("offer_price",ci.offer_price);
                              singleobject.put("qty",ci.Qty+"");
                              singleobject.put("photo",ci.image);

                              jsonarr.put(singleobject);
                          }

            //also join address details
            address_detail=MyClient.house_no+"\n"+MyClient.street+"\n"+MyClient.city+"\n"+MyClient.state+"\n"+MyClient.landmark;
            Date d = new java.util.Date();
            Date today = new Date(d.getTime());
            booking_date = today.toString();
            delivery_date=booking_date;

            tv1.setText(MyClient.order_id_g);
            tv2.setText(MyClient.net_amt_g+"");
            tv3.setText(MyClient.taxes_g+"");
            tv4.setText(MyClient.vat_g+"");
            tv5.setText(MyClient.delivery_charges_g+"");
            tv6.setText(MyClient.net_amt_payableg+"");
            tv7.setText(booking_date);
            tv8.setText(delivery_date);
            tv9.setText(address_detail);
            tv10.setText(MyClient.time);

            JSONObject ans=new JSONObject();
            ans.put("shoppinglist", jsonarr);

            ans.put("net_amt",MyClient.net_amt_g+"");
            ans.put("vat",MyClient.vat_g+"");
            ans.put("taxes",MyClient.taxes_g+"");
            ans.put("delivery_charges",MyClient.delivery_charges_g+"");
            ans.put("net_amt_payable",(MyClient.net_amt_payableg)/100+"");

            String tempemail=MyClient.loggedinemail.replace("%40","@");

            ans.put("email",tempemail);

            ans.put("address_detail",address_detail);

            ans.put("booking_date",booking_date);
            ans.put("delivery_date",delivery_date);

            final String postdata=ans.toString();
            Log.d("MYMESSAGE",postdata);


            new Thread(new Runnable() {
                @Override
                public void run()
                {

                    try {
                        //NETWORK CODE
                        //ADD Code for httpurlconnection
                        final String urlpath = MyClient.SERVERPATH + "M_dbentry";
                        URL url = new URL(urlpath);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        //StringBuilder postData = new StringBuilder();

                        //postData.append("un=abc&ps=123");

                        //Log.d("MYMESSAGE","data to be sent "+postData.toString());

                        byte[] postDataBytes = postdata.toString().getBytes("UTF-8");
                        int noofbytes = postDataBytes.length;

                        //set methods and headers
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);

                        //connection.setRequestProperty("Content-Type", "multipart/form-data");
                        connection.setRequestProperty("Content-Length", noofbytes + "");
                        //connection.setRequestProperty("filename", filename);
                        OutputStream outputStream = connection.getOutputStream();

                        outputStream.write(postDataBytes, 0, noofbytes);

                        //send postdata
            /*
            FileInputStream fis=new FileInputStream(pathoffiletobeuploaded);
            OutputStream outputStream=connection.getOutputStream();
            byte b[]=new byte[10000];
            int r;
            long count=0;

            while(true)
            {
                r=fis.read(b,0,10000);
                outputStream.write(b,0,r);
                count=count+r;

                Log.d("MYMESSAGE",count+"<-->"+filesize);

                if(count==filesize)
                {
                    break;
                }
            }
            */

                        outputStream.flush();

                        int resCode = connection.getResponseCode();

                        if (resCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            StringBuffer sb = new StringBuffer();

                            while (true) {
                                String s = br.readLine();
                                Log.d("MYMESSAGE", "res ->" + s);
                                if (s == null) {
                                    break;
                                }
                                sb.append(s);
                            }


                            //This time its simple String Data, But it can be JSON
                            final String serverans = sb.toString();
                            Log.d("MYMESSAGE", "ANSWER FROM SERVER " + serverans);

                            //Now render data in GUI
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), serverans, Toast.LENGTH_SHORT).show();
                                }
                            });


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
                        //Network Code Ends Here
                    }
                    catch(Exception ex)
                    {
                       ex.printStackTrace();
                    }
                }
            }).start();


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }

    }

