package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class user_home extends AppCompatActivity
{

    NavigationView nav;
    DrawerLayout dl;
    ActionBarDrawerToggle toggle;
    ImageView bt777,imv999;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        nav=(NavigationView)findViewById(R.id.nav);
        dl=(DrawerLayout)findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle=new ActionBarDrawerToggle(this,dl,R.string.drawer_opened,R.string.drawer_closed);
        dl.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().hide();

        //Now Add First Fragment
        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.ll1, new carousel_frag());
        ft.commitNow();

        //Find Button on mylayout which is part of fragment
        bt777=(ImageView)(findViewById(R.id.bt777));

        bt777.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(dl.isDrawerOpen(GravityCompat.START))
                {
                    dl.closeDrawer(GravityCompat.START);
                }
                else
                {
                    dl.openDrawer(GravityCompat.START);
                }
            }
        });

        imv999=(ImageView)(findViewById(R.id.imv999));
        imv999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),ShoppingCartDetail.class);
                startActivity(in);
            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_item1:

                        FragmentManager fm =  getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        ft.remove(MyClient.selectedfragment);
                        ft.commitNow();
                        Log.d("MYMESSAGE",MyClient.selectedfragment.toString()+" REMOVED");


                        FragmentTransaction ft3 = fm.beginTransaction();
                        ft3.add(R.id.ll1,new carousel_frag());
                        ft3.commitNow();
                        Log.d("MYMESSAGE","Carousal Frag ADDED");

                        break;

                    case R.id.nav_item2:
                        Intent in1 = new Intent(getApplicationContext(),search.class);
                        startActivity(in1);
                        break;

                    case R.id.nav_item3:
                        Intent in = new Intent(getApplicationContext(),myorders.class);
                        startActivity(in);
                        break;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });



    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            if(dl.isDrawerOpen(GravityCompat.START))
            {
                dl.closeDrawer(GravityCompat.START);
            }
            else
            {
                dl.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }




}
