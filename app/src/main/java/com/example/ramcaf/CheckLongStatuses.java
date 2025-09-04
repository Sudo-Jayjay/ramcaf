package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckLongStatuses extends AppCompatActivity {

    ImageView imgLTable1Seat1, imgLTable1Seat2, imgLTable1Seat3, imgLTable1Seat4;
    ImageView imgLTable2Seat1, imgLTable2Seat2, imgLTable2Seat3, imgLTable2Seat4;
    ImageView imgLTable3Seat1, imgLTable3Seat2, imgLTable3Seat3;

    String[] checkTableNumber = {};
    String[] checkSeatNumber = {};
    String[] checkStatus = {};

    int i = 0;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_long_statuses);

        //TABLE 1
        imgLTable1Seat1 = findViewById(R.id.imgLTable1Seat1);
        imgLTable1Seat2 = findViewById(R.id.imgLTable1Seat2);
        imgLTable1Seat3 = findViewById(R.id.imgLTable1Seat3);
        imgLTable1Seat4 = findViewById(R.id.imgLTable1Seat4);

        //TABLE 2
        imgLTable2Seat1 = findViewById(R.id.imgLTable2Seat1);
        imgLTable2Seat2 = findViewById(R.id.imgLTable2Seat2);
        imgLTable2Seat3 = findViewById(R.id.imgLTable2Seat3);
        imgLTable2Seat4 = findViewById(R.id.imgLTable2Seat4);

        //TABLE 3
        imgLTable3Seat1 = findViewById(R.id.imgLTable3Seat1);
        imgLTable3Seat2 = findViewById(R.id.imgLTable3Seat2);
        imgLTable3Seat3 = findViewById(R.id.imgLTable3Seat3);

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
            Object result = new FetchLongSeatStatuses().execute ().get ();

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

                String check_table = jo.getString ("long_table_number");
                String check_seat = jo.getString ("long_seat_number");
                String check_status = jo.getString ("long_seat_status");

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
                        imgLTable1Seat1.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable1Seat2.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable1Seat3.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable1Seat4.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable1Seat1.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable1Seat2.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable1Seat3.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable1Seat4.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable1Seat1.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable1Seat2.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable1Seat3.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable1Seat4.setColorFilter(0xFFFF6900);
                    }
                }
                break;

            case "2":
                if(checkStatus[x].equals("0")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable2Seat1.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable2Seat2.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable2Seat3.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable2Seat4.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable2Seat1.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable2Seat2.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable2Seat3.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable2Seat4.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable2Seat1.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable2Seat2.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable2Seat3.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("4")){
                        imgLTable2Seat4.setColorFilter(0xFFFF6900);
                    }
                }
                break;

            case "3":
                if(checkStatus[x].equals("0")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable3Seat1.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable3Seat2.setColorFilter(Color.GREEN);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable3Seat3.setColorFilter(Color.GREEN);
                    }
                }else if(checkStatus[x].equals("1")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable3Seat1.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable3Seat2.setColorFilter(Color.RED);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable3Seat3.setColorFilter(Color.RED);
                    }
                }else if(checkStatus[x].equals("2")){
                    if(checkSeatNumber[x].equals("1")){
                        imgLTable3Seat1.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("2")){
                        imgLTable3Seat2.setColorFilter(0xFFFF6900);
                    }else if(checkSeatNumber[x].equals("3")){
                        imgLTable3Seat3.setColorFilter(0xFFFF6900);
                    }
                }
                break;
        }
    }



}