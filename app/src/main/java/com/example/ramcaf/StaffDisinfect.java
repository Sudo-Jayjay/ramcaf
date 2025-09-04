package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class StaffDisinfect extends AppCompatActivity {

    Button btnToAvailable;
    TextView txtTableNumber, txtSeatNumber, txtStatus;

    String url;

    String tableNumber, seatNumber, tableType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_disinfect);

        btnToAvailable = findViewById(R.id.btnToAvailable);
        txtSeatNumber = findViewById(R.id.txtSeatNumber);
        txtTableNumber = findViewById(R.id.txtTableNumber);
        txtStatus = findViewById(R.id.txtStatus);


        SharedPreferences getInfo = getSharedPreferences("StaffPrefs",MODE_PRIVATE);
        tableNumber = getInfo.getString("tableNo", "");
        seatNumber = getInfo.getString("seatNo", "");
        tableType = getInfo.getString("tableType", "");

//        Toast.makeText(StaffDisinfect.this, "Table number = " + tableNumber + " Seat " +
//                "number = " + seatNumber, Toast.LENGTH_SHORT).show();

        if(tableType.equals("Short")){
            url = "https://ramcaf.000webhostapp.com/ramcaf/setShortToAvailable.php";
        }else if(tableType.equals("Long")){
            url = "https://ramcaf.000webhostapp.com/ramcaf/setLongToAvailable.php";
        }

        txtTableNumber.setText("Table " + tableNumber);
        txtSeatNumber.setText("Seat " + seatNumber);
        //txtTableNumber.setText("Table " + tableNumber);

        btnToAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(StaffDisinfect.this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Hitesh"," "+response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Hitesh",""+error);
                        Toast.makeText(StaffDisinfect.this, ""+error, Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> stringMap = new HashMap<>();
                        stringMap.put("tableNumber", tableNumber);
                        stringMap.put("seatNumber", seatNumber);
                        return stringMap;
                    }
                };

                requestQueue.add(stringRequest);

                Intent backToHome = new Intent(StaffDisinfect.this, StaffHome.class);
                startActivity(backToHome);
                finish();
            }
        });
    }
}