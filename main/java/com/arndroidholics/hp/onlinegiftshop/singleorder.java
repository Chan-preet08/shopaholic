package com.arndroidholics.hp.onlinegiftshop;



public class singleorder
{
    String net_amt;
    String booking_date;
    String delivery_date;
    String address_detail;
    String order_id;
    String nooforders;
    singleorder(String net_amt, String booking_date, String delivery_date,String address_detail,String order_id,String nooforders)
    {
        this.net_amt=net_amt;
        this.booking_date=booking_date;
        this.delivery_date=delivery_date;
        this.address_detail=address_detail;
        this.order_id=order_id;
        this.nooforders=nooforders;
    }
}
