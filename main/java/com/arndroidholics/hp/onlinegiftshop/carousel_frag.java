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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class carousel_frag extends Fragment
{
    CarouselView cv;
    ArrayList<singlecategory> al1;
    ListView lv1;
    myAdapter ad;
    ArrayList<String> al = new ArrayList<>();
    TextView tv500;

    public carousel_frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carousel_frag, container, false);

    }

    public void onResume()
    {
        super.onResume();
        cv = (CarouselView) getActivity().findViewById((R.id.cv));
        al1 = new ArrayList<>();
        ad = new myAdapter();
        lv1 = (ListView) getActivity().findViewById(R.id.lv1);
        lv1.setAdapter(ad);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                 MyClient.tempvalue =  al1.get(position).name;

                FragmentManager fm =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.remove(carousel_frag.this);
                ft.commitNow();

                FragmentTransaction ft2 = fm.beginTransaction();

                ft2.add(R.id.ll1, new insidefrag());

                ft2.commitNow();
            }
        });


        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tv500=(TextView)(getActivity().findViewById(R.id.tv500));
        tv500.setText(MyClient.alglob.size()+"");

        //SET GLOBAL FRAG VALUE
        MyClient.selectedfragment=this;


        //Thread to Fetch Caraousal Images
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Network Code
                try {
                    final String urlpath = MyClient.SERVERPATH + "json_carousel";
                    URL url = new URL(urlpath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resCode = 0;
                    resCode = connection.getResponseCode();
                    Log.d("MYMSG", resCode + "      res code");
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


                        Log.d("MYMSG", sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("mypics");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));

                            String photopath = singleobj.getString("photo");
                            Log.d("MYMSG", photopath);

                            if (photopath.startsWith(".")) {
                                photopath = photopath.substring(1);
                            }

                            photopath = photopath.replace(" ", "%20");
                            Log.d("MYMSG", photopath);

                            al.add(photopath);
                        }


                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            cv.setImageListener(new ImageListener()
                            {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    //imageView.setImageResource(imageIds[position]);

                                    Log.d("MYMSG", MyClient.SERVERPATH + al.get(position));

                                    Picasso.with(getContext()).load(MyClient.SERVERPATH + al.get(position)).resize(500, 150).into(imageView);
                                }
                            });

                            cv.setPageCount(al.size());

                            //cv.invalidate();
                        }
                    });


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }


            }

        }).start();


        // Thread to Fetch Categories
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "json_listview";
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

                        JSONArray jsonArray = jsonObject.getJSONArray("category");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String picpath = singleobj.getString("picpath");
                            String name = singleobj.getString("name");
                            String desc = singleobj.getString("desc");
                            Log.d("MYMSG", picpath + name + desc);
                            data = data + "picpath:" + picpath + "\tName : " + name + "\tdesc : " + desc + "\n";
                            al1.add(new singlecategory(picpath, desc, name));
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
                    return al1.size();
                }

                @Override
                public Object getItem(int position) {
                    return al1.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position*10;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    convertView= inflater.inflate(R.layout.single_catrow_design,parent,false);
                    singlecategory sc= al1.get(position);
                    ImageView imv1;
                    TextView tv1,tv2;
                    imv1=(ImageView)(convertView.findViewById(R.id.imv1));
                    tv1=(TextView)(convertView.findViewById(R.id.tv1));
                    tv2=(TextView)(convertView.findViewById(R.id.tv2));
                    String picpath=sc.picpath;
                    picpath = picpath.replace(" ", "%20");
                    Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVERPATH+picpath).resize(300,300).into(imv1);
                    tv1.setText(sc.name);
                    tv2.setText(sc.desc);
                    return convertView;
                }
            }


}
