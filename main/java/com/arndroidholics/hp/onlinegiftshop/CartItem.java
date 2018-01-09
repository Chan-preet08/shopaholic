package com.arndroidholics.hp.onlinegiftshop;

public class CartItem
{
    String p_id;
    String p_name;
    String mrp;
    String offer_price;
    int Qty;
    String desc;
    String image;
    CartItem(String p_id,String p_name,String mrp,String offer_price,int Qty,String desc,String image)
    {
        this.p_id=p_id;
        this.p_name=p_name;
        this.mrp=mrp;
        this.offer_price=offer_price;
        this.Qty=Qty;
        this.desc=desc;
        this.image=image;
    }
}
