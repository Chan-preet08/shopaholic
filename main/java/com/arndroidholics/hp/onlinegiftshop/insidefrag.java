package com.arndroidholics.hp.onlinegiftshop;


import android.os.Bundle;
import android.support.v4.app.*;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class insidefrag extends Fragment
{
 ArrayList<singlesubcat> al;
    ListView lv;
    myAdapter ad;
    TextView tv500;

    public insidefrag() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insidefrag, container, false);

    }

    public void onResume()
    {
        super.onResume();
        al = new ArrayList<>();
        ad = new myAdapter();
        lv = (ListView) getActivity().findViewById(R.id.lv);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MyClient.tempvalue =   al.get(position).subc_name;

                FragmentManager fm =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.remove(insidefrag.this);
                ft.commitNow();

                FragmentTransaction ft2 = fm.beginTransaction();
                ft2.add(R.id.ll1, new productfrag());
                ft2.commit();
            }
        });

        final String selectedcat= MyClient.tempvalue;

        tv500=(TextView)(getActivity().findViewById(R.id.tv500));
        tv500.setText(MyClient.alglob.size()+"");

        //SET GLOBAL FRAG VALUE
        MyClient.selectedfragment=this;

        // Thread to Fetch Categories
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "json_inlistview?catname="+selectedcat;
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

                        JSONArray jsonArray = jsonObject.getJSONArray("sub_category");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String picpath = singleobj.getString("picpath");
                            String c_name = singleobj.getString("c_name");
                            String subc_name = singleobj.getString("subc_name");
                            Log.d("MYMSG", picpath + c_name + subc_name);
                            data = data + "picpath:" + picpath + "\tc_name : " + c_name + "\tsubc_name : " + subc_name + "\n";
                            al.add(new singlesubcat(picpath, c_name, subc_name));
                            Log.d("MYMESSAGE", data);
                            //GUI Update Logic
                            getActivity().runOnUiThread(new Runnable() {
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
            convertView= inflater.inflate(R.layout.single_subcat_design,parent,false);
            singlesubcat sc= al.get(position);
            ImageView imv1;
            TextView tv1,tv2;
            imv1=(ImageView)(convertView.findViewById(R.id.imv1));
            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            String picpath=sc.picpath;
            picpath = picpath.replace(" ", "%20");
            Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVERPATH+picpath).resize(300,300).into(imv1);
            tv1.setText(sc.subc_name);
            tv2.setText(sc.c_name);
            return convertView;
        }
    }


}
