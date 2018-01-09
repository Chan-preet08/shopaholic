package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

public class search extends AppCompatActivity {
    EditText et1;
    ImageView imv1;
    ListView lv;
    myAdapter ad;
    ArrayList<singlesearch> al;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et1 = (EditText) findViewById(R.id.et1);
        imv1 = (ImageView) findViewById(R.id.imv1);
        lv = (ListView) findViewById(R.id.lv);
        al = new ArrayList<>();
        ad = new myAdapter();
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        imv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = et1.getText().toString();
                fetchproductsonline();
            }
        });
//
//        search = et1.getText().toString();
//        fetchproductsonline();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = et1.getText().toString();
                fetchproductsonline();
             }
        });

    }

    public void fetchproductsonline() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "M_search?search=" + search;
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

                        JSONArray jsonArray = jsonObject.getJSONArray("searchresults");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String p_name = singleobj.getString("p_name");
                            String desc = singleobj.getString("desc");
                            String mrp = singleobj.getString("mrp");
                            String offer_price = singleobj.getString("offer_price");
                            String image = singleobj.getString("image");


                            Log.d("MYMSG", p_name + desc + mrp + offer_price + image);
                            data = data + "p_name:" + p_name + "\tdesc : " + desc + "\tmrp : " + mrp +
                                    "\toffer_price : " + offer_price + "\timage : " + image + "\n";
                            al.add(new singlesearch(image, p_name, mrp, offer_price));
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


    class myAdapter extends BaseAdapter {

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
            return position * 10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.single_search_design, parent, false);
            singlesearch sc = al.get(position);

            ImageView imv1;
            TextView tv1, tv2, tv3;
            tv1 = (TextView) (convertView.findViewById(R.id.tv1));
            tv2 = (TextView) (convertView.findViewById(R.id.tv2));
            tv3 = (TextView) (convertView.findViewById(R.id.tv3));
            imv1 = (ImageView) (convertView.findViewById(R.id.imv1));
            String picpath = sc.picpath;
            picpath = picpath.replace(" ", "%20");
            Picasso.with(getApplicationContext()).load(MyClient.SERVERPATH + picpath).resize(300, 300).into(imv1);
            tv1.setText(sc.p_name);
            tv2.setText(sc.mrp);
            tv2.setPaintFlags(tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv3.setText(sc.offer_price);
            return convertView;
        }
    }
}
