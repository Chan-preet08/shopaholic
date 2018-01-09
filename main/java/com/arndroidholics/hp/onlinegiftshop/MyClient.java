package com.arndroidholics.hp.onlinegiftshop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MyClient
{
    public static String SERVERPATH="http://192.168.43.254:8084/onlinegiftshop/";
    public static String SERVERIP="192.168.43.254";
    public static int SERVERPORT=8084;
    public static String SERVERAPPNAME="/onlinegiftshop/";
    public static String tempvalue;
    public static Fragment selectedfragment;
    public static String loggedinemail;
    public static double net_amt_payableg;
    public static double net_amt_g;
    public static double delivery_charges_g;
    public static double vat_g;
    public static double taxes_g;
    public static String order_id_g;
    public static String time;


    public static ArrayList<CartItem> alglob = new ArrayList<>();

    public static String house_no;
    public static String street;
    public static String landmark;
    public static String city;
    public static String state;

}
