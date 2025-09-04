package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckShortStatuses extends AppCompatActivity {

    ImageView imgSTable1Seat1, imgSTable1Seat2;
    ImageView imgSTable2Seat1;
    ImageView imgSTable3Seat1, imgSTable3Seat2;

    String[] checkTableNumber = {};
    String[] checkSeatNumber = {};
    String[] checkStatus = {};

    int i = 0;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_short_statuses);

        //TABLE 1
        imgSTable1Seat1 = findViewById(R.id.imgSTable1Seat1);
        imgSTable1Seat2 = findViewById(R.id.imgSTable1Seat2);

        //TABLE 2
        imgSTable2Seat1 = findViewById(R.id.imgSTable2Seat1);

        //TABLE 3
        imgSTable3Seat1 = findViewById(R.id.imgSTable3Seat1);
        imgSTable3Seat2 = findViewById(R.id.imgSTable3Seat2);

        fetchStatusData();

        for(x = 0; x < checkTableNumber.length; x++){
            getStatuses(x);
        }

        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Where you call the database
                                fetchStatusData();

                                for(x = 0; x < checkTableNumber.length; x++){
                                    getStatuses(x);
                                }



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

    public void fetchStatusData() {
        try {
            //create a java class that inherits / super class is asynctask
            //then call the created class here and get the result
            Object result = new FetchShortSeatStatuses().execute ().get ();

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


            for (i = 0; i < ja.length (); i++) {
                JSONObject jo = ja.getJSONObject (i);

                String check_table = jo.getString ("short_table_number");
                String check_seat = jo.getString ("short_seat_number");
                String check_status = jo.getString ("short_seat_status");

                //push data in their respective array variable
                checkTableNumber = ArrayHelper.push (checkTableNumber, check_table);
                checkSeatNumber = ArrayHelper.push (checkSeatNumber, check_seat);
                checkStatus = ArrayHelper.push (checkStatus, check_status);
            }

        } catch (Exception ex) {
            Log.d ("Conversion ERROR", ex.getMessage ());
        }

    }

    public void getStatuses(int x){
        switch (checkTableNumber[x]){
            case "1":
                if(checkStatus[x].equals("0")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable1Seat1.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable1Seat2.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable1Seat1.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable1Seat2.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable1Seat1.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable1Seat2.setColorFilter(0xFFFF6900);
                    }
                }
                break;

            case "2":
                if(checkStatus[x].equals("0")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable2Seat1.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable2Seat1.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable2Seat1.setColorFilter(0xFFFF6900);
                    }
                }
                break;

            case "3":
                if(checkStatus[x].equals("0")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable3Seat1.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable3Seat2.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable3Seat1.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable3Seat2.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgSTable3Seat1.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgSTable3Seat2.setColorFilter(0xFFFF6900);
                    }
                }
                break;
        }
    }



}