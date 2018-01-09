package com.arndroidholics.hp.onlinegiftshop;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class address_screen extends AppCompatActivity
{
    EditText et1,et2,et3,et4,et5;
    Button bt;
    ImageView imv999;
    TextView tv500;

    Button bt501;
    TextView tv1234;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_screen);

        et1=(EditText) findViewById(R.id.et1);
        et2=(EditText) findViewById(R.id.et2);
        et3=(EditText) findViewById(R.id.et3);
        et4=(EditText) findViewById(R.id.et4);
        et5=(EditText) findViewById(R.id.et5);
        bt=(Button)findViewById(R.id.bt666);
        imv999=(ImageView)findViewById(R.id.imv999);
        tv500=(TextView)findViewById(R.id.tv500);
        getSupportActionBar().hide();
        imv999.setVisibility(View.GONE);
        tv500.setVisibility(View.GONE);

        bt501=(Button)(findViewById(R.id.bt501));
        tv1234=(TextView)(findViewById(R.id.tv1234));

        final TimePickerDialog dl=new TimePickerDialog(address_screen.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(address_screen.this,hourOfDay+":"+minute,Toast.LENGTH_SHORT).show();
                tv1234.setText(hourOfDay+":"+minute);

                //set global value here
                MyClient.time=tv1234.getText().toString();

                //dl.dismiss();
            }
        },12,30,false);

        dl.setTitle("Select Delivery Time");

        bt501.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dl.show();
            }
        });
    }

    public void go2(View v)
    {
        MyClient.house_no=et1.getText().toString();
        MyClient.street=et2.getText().toString();
        MyClient.landmark=et3.getText().toString();
        MyClient.city=et4.getText().toString();
        MyClient.state=et5.getText().toString();
        Intent in = new Intent(this,PaymentScreen.class);
        startActivity(in);
    }
}
