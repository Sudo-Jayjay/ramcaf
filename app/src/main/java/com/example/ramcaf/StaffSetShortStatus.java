package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffSetShortStatus extends AppCompatActivity {

    ListView statusListView;
    ArrayAdapter adapter;
    ArrayList<String> statusList = new ArrayList<>();


    //String url = "https://ramcaf.000webhostapp.com/ramcaf/staffGetStatus.php";

    int contentCount = 0;
    int refreshCount = 0;
    int i = 0;


    //array variables
    String [] tableNumber = {};
    String [] seatNumber = {};

    ArrayList<String> tableList =  new ArrayList<>();
    ArrayList<String> seatList =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_set_short_status);

        statusListView = findViewById(R.id.shortStatusList);

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
                                adapter = new ArrayAdapter(StaffSetShortStatus.this, android.R.layout.simple_list_item_1, statusList);

                                statusList.clear();
                                tableList.clear();
                                seatList.clear();
                                adapter.clear();

                                fetchData();

                                for(int i = 0; i < tableNumber.length; i++){
                                    statusList.add("Table Number " + tableNumber[i] + ", " + "Seat Number " + seatNumber[i]);
                                    tableList.add(tableNumber[i]);
                                    seatList.add(seatNumber[i]);
                                }

                                adapter = new ArrayAdapter(StaffSetShortStatus.this, R.layout.status_list_layout, statusList);
                                statusListView.setAdapter(adapter);


                                tableNumber = new String[0];
                                seatNumber = new String[0];

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();





        statusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sendInfo = getSharedPreferences("StaffPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editStaffInfo = sendInfo.edit();
                editStaffInfo.putString("tableNo", tableList.get(position));
                editStaffInfo.putString("seatNo", seatList.get(position));
                editStaffInfo.putString("tableType", "Short");
                editStaffInfo.commit();

                Intent toSetStatus = new Intent(StaffSetShortStatus.this, StaffDisinfect.class);
                startActivity(toSetStatus);
            }
        });


    }

    //create a method that will connect to the URL to fetch data and to fill the variables
    public void fetchData() {
        try {
            //create a java class that inherits / super class is asynctask
            //then call the created class here and get the result
            Object result = new FetchShortDisinfectionStatus().execute ().get ();

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

                String table_number = jo.getString ("short_table_number");
                String seat_number = jo.getString ("short_seat_number");

                //push data in their respective array variable
                tableNumber = ArrayHelper.push (tableNumber, table_number);
                seatNumber = ArrayHelper.push (seatNumber, seat_number);

            }

        } catch (Exception ex) {
            Log.d ("Conversion ERROR", ex.getMessage ());
        }

    }

}