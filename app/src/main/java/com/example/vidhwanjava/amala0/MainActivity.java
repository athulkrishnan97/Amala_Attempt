package com.example.vidhwanjava.amala0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


import java.util.ArrayList;

import java.util.List;

import xdroid.toaster.Toaster;

public class MainActivity extends AppCompatActivity {
    ListView listView1;
    ListView listView2;
    ListView listView3;


    String code;
    String hopapatient;
    String scanimage;

    List<String> codeArray;
    List<String> hopapatientArray;
    List<String> scanimageArray;
    String words = "";
    ArrayAdapter<String> firstListAdapter;
    ArrayAdapter<String> secondListAdapter;
    ArrayAdapter<String> thirdListAdapter;
    int firstRunFlag = 1;
    int toastPendingFlag = 0;
    Thread refreshThread;
    /*TextView waitingTopTextView;
    TextView proceedTopTextView;
    TextView dispatchTopTextView;
    */

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
                                                                    //
    //Control Variables                                            //
                                                                    //
    int variableForControllingSpeed = 1500;                         //
    int refreshDelay=5000;                                          //
                                                                    //
                                                                    //
//////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////

    getJsonFile asyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView1 = findViewById(R.id.waitingList);
        listView2 = findViewById(R.id.proceedList);
        listView3 = findViewById(R.id.dispatchList);


        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //waitingTopTextView=findViewById(R.id.waitingTop);
        // proceedTopTextView=findViewById(R.id.proceedTop);
        //dispatchTopTextView=findViewById(R.id.dispatchTop);

        firstRunFlag = 1;



       /* int listViewSize = listView1.getCount();
        String listv=""+listViewSize;
        Toast.makeText(getApplicationContext(),listv,Toast.LENGTH_SHORT).show();*/


        // Calls the function getJsonFile to Get the json file from the link http://122.15.159.161:4422/webservice/scanimage/all

        asyncTask= new getJsonFile();
        asyncTask.execute();




        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView1.getLastVisiblePosition() - listView1.getHeaderViewsCount() -
                        listView1.getFooterViewsCount()) >= (firstListAdapter.getCount()-1)) {



                } else if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView1.getFirstVisiblePosition() == 0))) {

                    listView1.smoothScrollToPositionFromTop(1, 0, codeArray.size() * variableForControllingSpeed);


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView2.getLastVisiblePosition() - listView2.getHeaderViewsCount() -
                        listView2.getFooterViewsCount()) >= (firstListAdapter.getCount() - 1)) {
                    listView2.smoothScrollToPositionFromTop(0, 0, hopapatientArray.size() * variableForControllingSpeed);


                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView2.getFirstVisiblePosition() == 0)) {

                    listView2.smoothScrollToPositionFromTop(hopapatientArray.size(), 0, hopapatientArray.size() * variableForControllingSpeed);


                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView3.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView3.getLastVisiblePosition() - listView3.getHeaderViewsCount() -
                        listView3.getFooterViewsCount()) >= (firstListAdapter.getCount() - 1)) {
                    listView3.smoothScrollToPositionFromTop(0, 0, codeArray.size() * variableForControllingSpeed);


                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView3.getFirstVisiblePosition() == 0)) {

                    listView3.smoothScrollToPositionFromTop(scanimageArray.size(), 0, scanimageArray.size() * variableForControllingSpeed);


                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }


    // Function to set data from the json to the listviews. It cannot be written in onCreate, thus this function.
    public void useArrayAdapter() {


        firstListAdapter = new ArrayAdapter<String>(this, R.layout.custom_listfile,scanimageArray) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                tv.setTextColor(Color.WHITE);

                // Return the view
                return view;
            }
        };

        listView1.setAdapter(firstListAdapter);


        secondListAdapter = new ArrayAdapter<String>(this, R.layout.custom_listfile, hopapatientArray) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                tv.setTextColor(Color.WHITE);
                // Return the view
                return view;
            }
        };


        listView2.setAdapter(secondListAdapter);

        thirdListAdapter = new ArrayAdapter<String>(this, R.layout.custom_listfile, codeArray) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                tv.setTextColor(Color.WHITE);
                // Return the view
                return view;
            }
        };
        listView3.setAdapter(thirdListAdapter);


    }


    @SuppressLint("StaticFieldLeak")
    public class getJsonFile extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {

                //Document doc = Jsoup.connect("http://122.15.159.161:4422/webservice/scanimage/all").ignoreContentType(true).get();
                Document doc =Jsoup.connect("http://testforamala.atwebpages.com").ignoreContentType(true).get();
                words = doc.text();


            } catch (IOException e) {


                Toaster.toast("Could not Communicate with the Server. Ensure that the Network is up and Running(in getjsonfile)");



            }

            return null;
        }


        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);




                JsonParser();//parse the json file that is downloaded from the method getjsonfile



                useArrayAdapter();// set the ListView with the parsed json file using a adapter

                scroller1();
                scroller2();
                scroller3();
            /*waitingTopTextView.setText(codeArray.get(0));
            proceedTopTextView.setText(hopapatientArray.get(0));
            dispatchTopTextView.setText(scanimageArray.get(0));*/

            if (firstRunFlag == 1) {
                refreshFunction();
            }


        }
    }


    public void refreshFunction() {

        Runnable runnableForRefresh = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    try {
                        Thread.sleep(refreshDelay);
                        String newWords;
                        firstRunFlag = 0;
                        //Document newdoc = Jsoup.connect("http://122.15.159.161:4422/webservice/scanimage/all").ignoreContentType(true).get();
                        Document newdoc =Jsoup.connect("http://testforamala.atwebpages.com").ignoreContentType(true).get();
                        newWords = newdoc.text();


                        if (!words.equals(newWords)) {
                            new getJsonFile().execute();
                            toastPendingFlag = 1;
                            showRefreshToast();

                        }


                    } catch (IOException e) {

                        Toaster.toast("Could not Communicate with the Server.");
                        LaunchErrorActivity();

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


    public void LaunchErrorActivity() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                    startActivity(intent);
                    asyncTask.cancel(true);
                    finish();
                }
            });



    }




    public void scroller1() {
        Runnable waitingTread1 = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                downSmoothScrollerForListView1();


            }
        };
        Thread waitingThread11 = new Thread(waitingTread1);
        waitingThread11.start();
    }


    public void scroller2() {
        Runnable waitingTread2 = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                downSmoothScrollerForListView2();


            }
        };

        Thread waitingThread22 = new Thread(waitingTread2);
        waitingThread22.start();
    }


    public void scroller3() {

        Runnable waitingTread3 = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                downSmoothScrollerForListView3();


            }
        };
        Thread waitingThread33 = new Thread(waitingTread3);
        waitingThread33.start();

    }


    public void JsonParser() {


        codeArray = new ArrayList<>();
        //codeArray.sort();


        hopapatientArray = new ArrayList<>();
        scanimageArray = new ArrayList<>();
        try {
            JSONArray jsonResponse = new JSONArray(words);

            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject dataReceivedObject = jsonResponse.getJSONObject(i);
                code = dataReceivedObject.getString("code");
                codeArray.add(code);
                hopapatient = dataReceivedObject.getString("hopapatient");
                hopapatientArray.add(hopapatient);
                scanimage = dataReceivedObject.getString("scan" +
                        "" +
                        "image");
                scanimageArray.add(scanimage);

                //Toast.makeText(this,code,Toast.LENGTH_SHORT).show();
                //Toast.makeText(this,hopapatient,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {

            e.printStackTrace();
            Log.d("Error", e.toString());
            Toaster.toast("Json could not be parsed");
        }
    }


    public void showRefreshToast() {
        if (toastPendingFlag == 1) {

            Toaster.toast("Data Updated");
            toastPendingFlag = 0;
        }

    }


    public void downSmoothScrollerForListView1() {


        listView1.smoothScrollToPositionFromTop(codeArray.size(), 5, codeArray.size() * variableForControllingSpeed);


    }

    public void downSmoothScrollerForListView2() {


        listView2.smoothScrollToPositionFromTop(hopapatientArray.size(), 50, hopapatientArray.size() * variableForControllingSpeed);


    }

    public void downSmoothScrollerForListView3() {


        listView3.smoothScrollToPositionFromTop(scanimageArray.size(), 50, scanimageArray.size() * variableForControllingSpeed);


    }

    @Override
    public void onDestroy() {
        refreshThread.interrupt();
        super.onDestroy();
    }
    /*public void startNewActivity(View view) {
        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.teslacoilsw.launcher");
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + "com.teslacoilsw.launcher"));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }*/
}