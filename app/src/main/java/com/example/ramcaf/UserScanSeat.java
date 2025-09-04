package com.example.ramcaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

public class UserScanSeat extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView resultData, reminder;
    String scannedTableType, scannedTable, scannedSeat;

    String url, tableType;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scan_seat);

        scannerView = findViewById(R.id.qrCode);
        codeScanner = new CodeScanner(this, scannerView);
        resultData = findViewById(R.id.textView5);
        reminder = findViewById(R.id.txtReminder);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());
                        reminder.setVisibility(View.VISIBLE);

                        String[] split = result.getText().split(",");

                        scannedTableType = split[0];
                        scannedTable = split[1];
                        scannedSeat = split[2];

                        if(scannedTableType.equals("L")){
                            url = "https://ramcaf.000webhostapp.com/ramcaf/setLongToOccupied.php";
                            tableType = "Long";
                        }else if(scannedTableType.equals("S")){
                            url = "https://ramcaf.000webhostapp.com/ramcaf/setShortToOccupied.php";
                            tableType = "Short";
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        userID = sharedPreferences.getString("userid", "");

                        SharedPreferences userSendInfo = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editUserInfo = userSendInfo.edit();
                        editUserInfo.putString("tableNo", scannedTable);
                        editUserInfo.putString("seatNo", scannedSeat);
                        editUserInfo.putString("tableType", tableType);
                        editUserInfo.putString("userID", userID);
                        editUserInfo.commit();

                        setStatus();

                        Intent toUserSetStatus = new Intent(UserScanSeat.this, UserSeatStatus.class);
                        startActivity(toUserSetStatus);
                    }
                });

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera(){
        Dexter.withContext(UserScanSeat.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(UserScanSeat.this, "Camera Permission is Denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public void setStatus(){
        RequestQueue requestQueue = Volley.newRequestQueue(UserScanSeat.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Hitesh"," "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh",""+error);
                Toast.makeText(UserScanSeat.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("tableNumber", scannedTable);
                stringMap.put("seatNumber", scannedSeat);
                stringMap.put("userID", userID);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}