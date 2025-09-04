package com.example.ramcaf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class StaffHome extends AppCompatActivity {

    Button btnSetStatus, btnCheckStatus, btnStaffLogout;
    TextView txtStaffCafCount;

    AlertDialog alertdialog;

    String[] longCount ={};
    String[] shortCount ={};

    int cafCapacity = 0;
    int longCounter = 0;
    int shortCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        btnSetStatus = findViewById(R.id.btnStaffStatus);
        btnCheckStatus = findViewById(R.id.btnStatus);
        txtStaffCafCount =  findViewById(R.id.txtStaffCafCount);
        btnStaffLogout = findViewById(R.id.btnStaffLogout);

        btnSetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetStatus = new Intent(StaffHome.this, StaffChooseSetStatus.class);
                startActivity(toSetStatus);
            }
        });

        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckStatus = new Intent(StaffHome.this, CheckStatuses.class);
                startActivity(toCheckStatus);
            }
        });

        final SharedPreferences shared = (getSharedPreferences ("MySharedPref", Context.MODE_PRIVATE));
        final SharedPreferences shared1 = (getSharedPreferences ("StaffPrefs", Context.MODE_PRIVATE));

        btnStaffLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog = new AlertDialog.Builder(StaffHome.this).create();
                alertdialog.setTitle("Logout");
                alertdialog.setMessage("Are you sure you want to logout?");
                alertdialog.setCancelable(false);
                alertdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertdialog.dismiss();

                    }
                });

                alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.apply();

                        SharedPreferences.Editor editor1 = shared1.edit();
                        editor1.clear();
                        editor1.apply();

                        Intent intent = new Intent(StaffHome.this, Login.class);
                        startActivity(intent);

                    }
                });

                alertdialog.show();
            }
        });

        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Where you call the database
                                fetchLongStatusData();
                                fetchShortStatusData();

                                computeCount();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }

    public void fetchLongStatusData() {
        try {
            //create a java class that inherits / super class is asynctask
            //then call the created class here and get the result
            Object result = new FetchLongCount().execute ().get ();

            //use the result to fill our variables
            //1. convert string to JSON array since the record composed of many records
            //JSON (JavaScript Object Notation) object is like this
            /*
                {
                    name: "juan",
                    age: "18"
                }
             */
            JSONArray ja = new JSONArray (result.toString ());

            //2. Iterate the data of JSONArray and convert it to JSONObject;


            for (int i = 0; i < ja.length (); i++) {
                JSONObject jo = ja.getJSONObject (i);

                String long_count = jo.getString ("long_seat_status");

                //push data in their respective array variable
                longCount = ArrayHelper.push (longCount, long_count);

            }

        } catch (Exception ex) {
            Log.d ("Conversion ERROR", ex.getMessage ());
        }

    }

    public void fetchShortStatusData() {
        try {
            //create a java class that inherits / super class is asynctask
            //then call the created class here and get the result
            Object result = new FetchShortCount().execute ().get ();

            //use the result to fill our variables
            //1. convert string to JSON array since the record composed of many records
            //JSON (JavaScript Object Notation) object is like this
            /*
                {
                    name: "juan",
                    age: "18"
                }
             */
            JSONArray ja = new JSONArray (result.toString ());

            //2. Iterate the data of JSONArray and convert it to JSONObject;


            for (int i = 0; i < ja.length (); i++) {
                JSONObject jo = ja.getJSONObject (i);

                String short_count = jo.getString ("short_seat_status");

                //push data in their respective array variable
                shortCount = ArrayHelper.push (shortCount, short_count);

            }

        } catch (Exception ex) {
            Log.d ("Conversion ERROR", ex.getMessage ());
        }

    }

    public void computeCount(){

        longCounter = 0;
        shortCounter = 0;
        cafCapacity = 0;

        for(int i = 0; i < longCount.length; i++){
            if(longCount[i].equals("1")){
                longCounter++;
            }
        }

        for(int x = 0; x < shortCount.length; x++){
            if(shortCount[x].equals("1")){
                shortCounter++;
            }
        }

        cafCapacity = longCounter + shortCounter;
        txtStaffCafCount.setText("Caf capacity: " + cafCapacity);

        longCount = new String[0];
        shortCount = new String[0];

    }
}