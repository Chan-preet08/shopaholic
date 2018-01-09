package com.arndroidholics.hp.onlinegiftshop;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.*;
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


public class single_pro_carousel extends Fragment {
    ArrayList<String> al;
    CarouselView cv;
    TextView tv1,tv2,tv3,tv4,tv33333,tv22222;
    Button bt100;

    Button bt101,bt102;

    String picpath;
    TextView tv500;
    LinearLayout ll555;

    public single_pro_carousel() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_pro_carousel, container, false);

    }

    final String p_id = MyClient.tempvalue;

    //Thread to Fetch Caraousal Images
    public void onResume()
    {
        super.onResume();
        al = new ArrayList<>();
        cv = (CarouselView) getActivity().findViewById(R.id.cv);
        tv1 = (TextView) getActivity().findViewById(R.id.tv1111);
        tv2 = (TextView) getActivity().findViewById(R.id.tv2222);
        tv3 = (TextView) getActivity().findViewById(R.id.tv3333);
        tv4 = (TextView) getActivity().findViewById(R.id.tv4444);
        tv33333 = (TextView) getActivity().findViewById(R.id.tv33333);
        tv22222 = (TextView) getActivity().findViewById(R.id.tv22222);

        tv500=(TextView)(getActivity().findViewById(R.id.tv500));
        tv500.setText(MyClient.alglob.size()+"");

        bt100 = (Button) getActivity().findViewById(R.id.bt100);

        bt101 = (Button) getActivity().findViewById(R.id.bt101);
        bt102 = (Button) getActivity().findViewById(R.id.bt102);

        bt100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    String p_name= tv1.getText().toString();
                    String mrp = tv2.getText().toString();
                    String offer_price=tv3.getText().toString();
                    String desc= tv4.getText().toString();
                    MyClient.alglob.add(new CartItem(p_id,p_name,mrp,offer_price,1,desc,picpath));
                    Toast.makeText(getActivity(),"Size of Cart "+MyClient.alglob.size(),Toast.LENGTH_SHORT).show();

                    tv500.setText(MyClient.alglob.size()+"");

                    bt101.setVisibility(View.VISIBLE);
                    bt102.setVisibility(View.VISIBLE);

                    bt100.setVisibility(View.GONE);

                    ll555.invalidate();

            }
        });


        bt101.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity().getApplicationContext(),ShoppingCartDetail.class);
                startActivity(in);
            }
        });

        bt102.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent in=new Intent(getActivity().getApplicationContext(),);
                //startActivity(in);

                FragmentManager fm =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.remove(MyClient.selectedfragment);
                ft.commitNow();
                Log.d("MYMESSAGE",MyClient.selectedfragment.toString()+" REMOVED");


                FragmentTransaction ft3 = fm.beginTransaction();
                ft3.add(R.id.ll1,new carousel_frag());
                ft3.commitNow();
                Log.d("MYMESSAGE","Carousal Frag ADDED");

            }
        });

        ll555=(LinearLayout)(getActivity().findViewById(R.id.ll555));


        //SET GLOBAL FRAG VALUE
        MyClient.selectedfragment=this;

        try {
            Thread.sleep(500);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Network Code
                try {
                    final String urlpath = MyClient.SERVERPATH + "single_pro_carousel?p_id=" + p_id;
                    URL url = new URL(urlpath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resCode = 0;
                    resCode = connection.getResponseCode();
                    Log.d("mymmmm", resCode + "      res code");
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


                        Log.d("mymmmm", sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String photopath = singleobj.getString("picpath");
                            String caption = singleobj.getString("caption");
                            Log.d("mymmmm", photopath);

                            if (photopath.startsWith(".")) {
                                photopath = photopath.substring(1);
                            }
                            photopath = photopath.replace(" ", "%20");
                            Log.d("mymmmm", photopath);
                            al.add(photopath);
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cv.setImageListener(new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    //imageView.setImageResource(imageIds[position]);

                                    Log.d("mymmmm", MyClient.SERVERPATH + al.get(position));

                                    Picasso.with(getContext()).load(MyClient.SERVERPATH + al.get(position)).resize(500, 150).into(imageView);


                                }
                            });

                            cv.setPageCount(al.size());
                            cv.invalidate();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {
                    final String urlpath = MyClient.SERVERPATH + "single_pro_details?p_id="+p_id;
                    URL url = new URL(urlpath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resCode = 0;
                    resCode = connection.getResponseCode();
                    Log.d("mmm", resCode + "      res code");
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
                        Log.d("mmm", sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("product");
                        al.clear();

                        {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(0));
                            final String p_name = singleobj.getString("p_name");
                            final String mrp = singleobj.getString("mrp");
                            final String offer_price = singleobj.getString("offer_price");
                            picpath = singleobj.getString("picpath");
                            final String desc = singleobj.getString("desc");
                            Log.d("mmm", p_name + mrp+offer_price+desc);
                            data = data + "p_name : " + p_name + "\tmrp : " + mrp + "\tdesc : "+desc+"\toffer_price : "+offer_price+"\t"+picpath+"\n";
                            Log.d("mmm", data);

                            //GUI Update Logic
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("mmm", "DATA SET CHANGED");
                                    tv1.setText(p_name);
                                    tv22222.setText("MRP");
                                    tv2.setText(mrp);
                                    tv2.setPaintFlags(tv2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                                    tv33333.setText("Offer Price");
                                    tv3.setText(offer_price);
                                    tv4.setText(desc);
                                    Log.d("mmm","TV UPDATED");
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



}