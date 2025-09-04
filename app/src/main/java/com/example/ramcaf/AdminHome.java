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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdminHome extends AppCompatActivity {

    TextView txtHelloAdmin;
    Button btnAdminCheckStatus, btnAdminLogout;
    TextView txtAdminCafCount;

    AlertDialog alertdialog;

    String[] longCount ={};
    String[] shortCount ={};

    int cafCapacity = 0;
    int longCounter = 0;
    int shortCounter = 0;

    String url = "https://ramcaf.000webhostapp.com/ramcaf/getAdminName.php";
    //String name;
    String admin_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        txtHelloAdmin = findViewById(R.id.txtHelloAdmin);
        txtAdminCafCount = findViewById(R.id.txtAdminCafCount);
        btnAdminCheckStatus = findViewById(R.id.btnAdminCheckStatus);
        btnAdminLogout = findViewById(R.id.btnAdminLogout);


//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//        String admin_name = sharedPreferences.getString("username", "");
//        txtHelloAdmin.setText("Hello, " + admin_name + "!");

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        admin_id = sharedPreferences.getString("adminid", "");
        txtHelloAdmin.setText("Hello!");


        btnAdminCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckStatus = new Intent(AdminHome.this, CheckStatuses.class);
                startActivity(toCheckStatus);
            }
        });

        final SharedPreferences shared = (getSharedPreferences ("MySharedPref", Context.MODE_PRIVATE));

        btnAdminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog = new AlertDialog.Builder(AdminHome.this).create();
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

                        Intent intent = new Intent(AdminHome.this, Login.class);
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
        txtAdminCafCount.setText("Caf capacity: " + cafCapacity);

        longCount = new String[0];
        shortCount = new String[0];

    }
}