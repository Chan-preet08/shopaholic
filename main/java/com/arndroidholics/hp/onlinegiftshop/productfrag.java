package com.arndroidholics.hp.onlinegiftshop;


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


public class productfrag extends Fragment
{
    ArrayList<singleproduct> al;
    GridView gv;
    myAdapter ad;
    TextView tv500;

    public productfrag() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productfrag, container, false);

    }

    final String selectedsub_cat= MyClient.tempvalue.replace(" ","%20");

    public void onResume()
    {
        super.onResume();
        al = new ArrayList<>();
        ad = new myAdapter();
        gv=(GridView)(getActivity().findViewById(R.id.gv));
        gv.setAdapter(ad);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MyClient.tempvalue =   al.get(position).p_id;

                FragmentManager fm =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.remove(productfrag.this);
                ft.commitNow();

                //ft.replace(R.id.ll1, new single_pro_carousel());
                FragmentTransaction ft2 = fm.beginTransaction();

                ft2.add(R.id.ll1,new single_pro_carousel());
                ft2.commitNow();

                //ft.commit();


            }
        });

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


                    final String urlpath = MyClient.SERVERPATH + "pro_listview?subcat="+selectedsub_cat;
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

                        JSONArray jsonArray = jsonObject.getJSONArray("product");

                        al.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleobj = (JSONObject) (jsonArray.get(i));
                            String picpath = singleobj.getString("picpath");
                            String p_name = singleobj.getString("p_name");
                            String mrp = singleobj.getString("mrp");
                            String p_id = singleobj.getString("p_id");
                            String offer_price = singleobj.getString("offer_price");
                            Log.d("MYMSG", picpath +p_name+ mrp+offer_price);
                            data = data + "picpath:" + picpath + "\tp_name : " + p_name + "\tmrp : " + mrp + "\tp_id : "+p_id+"\toffer_price : "+offer_price+"\n";
                            al.add(new singleproduct(picpath, p_name, mrp,p_id,offer_price));
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
            convertView= inflater.inflate(R.layout.single_pro_design,parent,false);
            singleproduct sc= al.get(position);
            ImageView imv1;
            TextView tv1,tv2,tv3;
            imv1=(ImageView)(convertView.findViewById(R.id.imv1));
            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            tv3=(TextView)(convertView.findViewById(R.id.tv3));
            String picpath=sc.picpath;
            picpath = picpath.replace(" ", "%20");
            Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVERPATH+picpath).resize(300,300).into(imv1);
            tv1.setText(sc.p_name);
            tv2.setText(sc.mrp);
            tv2.setPaintFlags(tv2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            tv3.setText(sc.offer_price);
            return convertView;
        }
    }


}
