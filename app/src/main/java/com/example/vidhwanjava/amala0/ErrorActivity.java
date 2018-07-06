package com.example.vidhwanjava.amala0;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import xdroid.toaster.Toaster;

public class ErrorActivity extends AppCompatActivity {
    Thread refreshThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras.getString("key").equals("Network is up")) {
            finish();
        }
        Toaster.toast(extras.getString("key"));*/
        refreshFunction();

    }


    public void refreshFunction() {

        Runnable runnableForRefresh = new Runnable() {
            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {


                        try {
                            Thread.sleep(2000);






                            //Document newdoc = Jsoup.connect("http://122.15.159.161:4422/webservice/scanimage/all").ignoreContentType(true).get();
                            Document newdoc =Jsoup.connect("http://testforamala.atwebpages.com").ignoreContentType(true).get();
                            //newdoc is never used because we are only checking to whether an exception will be raised


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                            Toaster.toast("Could not Communicate with the Server");
                        }
                        catch (InterruptedException e){
                            Thread.currentThread().interrupt();

                        }



                }
            }
        };
        refreshThread = new Thread(runnableForRefresh);
        refreshThread.start();


    }

    @Override
    public void onDestroy() {
        refreshThread.interrupt();
        super.onDestroy();
    }
}