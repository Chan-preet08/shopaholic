package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class myorders extends AppCompatActivity
{
    ArrayList<singleorder> al;
    ListView lv;
    myAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        al = new ArrayList<>();
        ad = new myAdapter();
        lv = (ListView) findViewById(R.id.lv);

        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String order_id=al.get(position).order_id;
                MyClient.order_id_g=order_id;
                Intent in=new Intent(getApplicationContext(),mydorderdetails.class);
                in.putExtra("order_id",order_id);
                startActivity(in);
            }
        });

// Thread to Fetch Categories
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "M_myorders?email="+MyClient.loggedinemail;
                    URL url = new URL(urlpath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resCode = 0;
                    resCode = connection.getResponseCode();
                    Log.d("MYMSGNEW", resCode + "      res code");
                    StringBuffer sb = new StringBuffer();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        while (true) {
                            String s = br.readLine();

                            if (s == null) {
                                break;
                            }
                            sb.append(s);
                        }


                        Log.d("MYMSGNEW", sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("orders");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String net_amt = singleobj.getString("net_amt");
                            String booking_date = singleobj.getString("booking_date");
                            String delivery_date = singleobj.getString("delivery_date");
                            String address_detail = singleobj.getString("address_detail");
                            String order_id = singleobj.getString("order_id");
                            String nooforders = singleobj.getString("nooforders");


                            Log.d("MYMSG", net_amt + booking_date + delivery_date+address_detail+order_id+nooforders);
                            data = data + "net_amt:" + net_amt + "\tbooking_date : " + booking_date + "\tdelivery_date : " + delivery_date + "\taddress_detail : " + address_detail+"\torder_id : "+order_id +"\tnooforders :"+nooforders+"\n";
                            al.add(new singleorder(net_amt, booking_date, delivery_date,address_detail,order_id,nooforders));
                            Log.d("MYMESSAGE", data);
                            //GUI Update Logic
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ad.notifyDataSetChanged();
                                    Log.d("MYMESSAGE", "DATA SET CHANGED");
                                }
                            });
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }).start();
    }
    class myAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int position) {
            return al.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position*10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView= inflater.inflate(R.layout.single_myorder_design,parent,false);
            singleorder sc= al.get(position);

            TextView tv1,tv2,tv3,tv4,tv5,tv6;
            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            tv3=(TextView)(convertView.findViewById(R.id.tv3));
            tv4=(TextView)(convertView.findViewById(R.id.tv4));
            tv5=(TextView)(convertView.findViewById(R.id.tv5));
            tv6=(TextView)(convertView.findViewById(R.id.tv6));

            tv1.setText("Your Order ID :"+sc.order_id);
            tv2.setText("Net Amount :"+sc.net_amt);
            tv3.setText("No. Of Orders :"+sc.nooforders);
            tv4.setText("Shipping Address :"+sc.address_detail);
            tv5.setText("Booking date :"+sc.booking_date);
            tv6.setText("Delivery Date :"+sc.delivery_date);
            return convertView;
        }
    }

    }

