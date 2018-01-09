package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShoppingCartDetail extends AppCompatActivity
{
    ListView lv1;
    myAdapter ad;
    TextView tv19,tv20,tv21,tv22,tv23,tv24,tv220,tv221,tv222,tv223,tv224;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_detail);
        lv1=(ListView) findViewById(R.id.lv1);
        ad = new myAdapter();
        lv1.setAdapter(ad);
        tv19=(TextView)(findViewById(R.id.tv19));
        tv20=(TextView)(findViewById(R.id.tv20));
        tv21=(TextView)(findViewById(R.id.tv21));
        tv22=(TextView)(findViewById(R.id.tv22));
        tv23=(TextView)(findViewById(R.id.tv23));
        tv24=(TextView)(findViewById(R.id.tv24));
        tv220=(TextView)findViewById(R.id.tv220);
        tv221=(TextView)(findViewById(R.id.tv221));
        tv222=(TextView)(findViewById(R.id.tv222));
        tv223=(TextView)(findViewById(R.id.tv223));
        tv224=(TextView)(findViewById(R.id.tv224));

        tv19.setText("No of Items "+MyClient.alglob.size());

        getSupportActionBar().hide();
        int net_amt=0;
        for(int i=0;i<MyClient.alglob.size();i++)
        {
            net_amt=net_amt+ (MyClient.alglob.get(i).Qty* Integer.parseInt(MyClient.alglob.get(i).offer_price));
        }
        MyClient.net_amt_g=net_amt;
        tv220.setText("Amount");
        tv20.setText(net_amt+"");
        Double vat = net_amt * 0.05;
        MyClient.vat_g=vat;
        tv221.setText("Vat");
        tv21.setText(vat+"");
        Double taxes = net_amt * 0.12;
        MyClient.taxes_g=taxes;
        tv222.setText("Tax");
        tv22.setText(taxes+"");
        Double delivery_charges = 50.0;
        MyClient.delivery_charges_g=delivery_charges;
        tv223.setText("Delivery Charges");
        tv23.setText(delivery_charges+"");
        Double net_amt_payable = net_amt + vat + taxes + delivery_charges;
        tv224.setText("Total Amount");
        MyClient.net_amt_payableg = net_amt_payable;
        tv24.setText(net_amt_payable+"");
    }
    class myAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return MyClient.alglob.size();
        }

        @Override
        public Object getItem(int position) {
            return MyClient.alglob.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position*10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView= inflater.inflate(R.layout.singlerowcart,parent,false);
            CartItem ci= MyClient.alglob.get(position);
            ImageView imv1;
            TextView tv1,tv2,tv3,tv4;
            imv1=(ImageView)(convertView.findViewById(R.id.imv1));
            tv1=(TextView)(convertView.findViewById(R.id.tv1));
            tv2=(TextView)(convertView.findViewById(R.id.tv2));
            tv3=(TextView)(convertView.findViewById(R.id.tv3));
            tv4=(TextView)(convertView.findViewById(R.id.tv4));
            String picpath=ci.image;
            picpath = picpath.replace(" ", "%20");
            Picasso.with(getApplicationContext()).load(MyClient.SERVERPATH+picpath).resize(300,300).into(imv1);
            tv1.setText(ci.p_name);
            tv2.setText(ci.desc);
            tv3.setText(ci.mrp);
            tv3.setPaintFlags(tv2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            tv4.setText(ci.offer_price);
            return convertView;
        }
    }
public void go(View v)
{
    Intent in = new Intent(this,address_screen.class);
    startActivity(in);
}
}
