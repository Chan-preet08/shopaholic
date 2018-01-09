package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
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

public class mydorderdetails extends AppCompatActivity {
    ArrayList<singleorderdetail> al;
    ListView lv;
    myAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydorderdetails);

        final String order_id=getIntent().getStringExtra("order_id");

        al = new ArrayList<>();
        ad = new myAdapter();
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

            }
        });

// Thread to Fetch Categories
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "M_myordersdetails?order_id="+order_id;
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

                        JSONArray jsonArray = jsonObject.getJSONArray("orderdetails");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String product_name = singleobj.getString("product_name");
                            String qty = singleobj.getString("qty");
                            String offer_price = singleobj.getString("offer_price");
                            String photo = singleobj.getString("photo");


                            Log.d("MYMSG", product_name + qty + offer_price+photo);
                            data = data + "product_name:" + product_name + "\tqty : " + qty + "\toffer_price : " + offer_price + "\tphoto : " + photo+"\n";
                            al.add(new singleorderdetail(product_name, offer_price, qty,photo));
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
            convertView= inflater.inflate(R.layout.single_orderdetail_design,parent,false);
            singleorderdetail sc= al.get(position);
            ImageView imv1;
            TextView tv1,tv2,tv3,tv4,tv5,tv6;
            imv1 = (ImageView)(convertView.findViewById(R.id.imv1));
            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            tv3=(TextView)(convertView.findViewById(R.id.tv3));
            String picpath=sc.photo;
            picpath = picpath.replace(" ", "%20");
            Picasso.with(getApplicationContext()).load(MyClient.SERVERPATH+picpath).resize(300,300).into(imv1);
            tv1.setText("Product Name :"+sc.product_name);
            tv2.setText("Quantity :"+sc.qty);
            tv3.setText("Price :"+sc.offer_price);
            return convertView;
        }


    }
}
