package com.arndroidholics.hp.onlinegiftshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;


public class PaymentScreen extends AppCompatActivity {

    WebView wv1;
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);

        MyClient.net_amt_payableg=MyClient.net_amt_payableg*100;   //Convert to Paise, since payment gateway uses paise

        wv1=(WebView)(findViewById(R.id.wv1));
        wv1.loadUrl(MyClient.SERVERPATH+"payment.jsp?amount="+MyClient.net_amt_payableg);
        wv1.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = wv1.getSettings();
        webSettings.setJavaScriptEnabled(true);

        bt1=(Button)(findViewById(R.id.bt1));
        getSupportActionBar().hide();
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {


            return  false;
            /*
            if (Uri.parse(url).getHost().equals("http://192.168.225.49:8084/ServerSideForMobileApp/test.html"))
            {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }

            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
            */
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            //super.onPageFinished(view, url);
            Log.d("MYMESSAGE","LOADING ->"+url);

            if(url.equalsIgnoreCase(MyClient.SERVERPATH+"success.jsp"))
            {
                bt1.setVisibility(View.VISIBLE);

            }
        }
    }

    public void go(View v)
    {
        Intent in=new Intent(this,OrderConfirmation.class);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        if(wv1.canGoBack())
        {
            wv1.goBack();
        }
    }
}
