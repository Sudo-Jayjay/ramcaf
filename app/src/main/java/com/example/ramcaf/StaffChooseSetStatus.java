package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffChooseSetStatus extends AppCompatActivity {

    Button btnStaffSetLong, btnStaffSetShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_choose_set_status);

        btnStaffSetLong = findViewById(R.id.btnStaffSetLong);
        btnStaffSetShort = findViewById(R.id.btnStaffSetShort);

        btnStaffSetLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetLong = new Intent(StaffChooseSetStatus.this, StaffSetLongStatus.class);
                startActivity(toSetLong);
            }
        });

        btnStaffSetShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetShort = new Intent(StaffChooseSetStatus.this, StaffSetShortStatus.class);
                startActivity(toSetShort);
            }
        });
    }
}