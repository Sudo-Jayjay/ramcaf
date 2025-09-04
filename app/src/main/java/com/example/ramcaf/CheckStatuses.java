package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckStatuses extends AppCompatActivity {

    Button btnCheckLong, btnCheckShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_statuses);

        btnCheckLong = findViewById(R.id.btnCheckLong);
        btnCheckShort = findViewById(R.id.btnCheckShort);


        btnCheckLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckLong = new Intent(CheckStatuses.this, CheckLongStatuses.class);
                startActivity(toCheckLong);
            }
        });

        btnCheckShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckShort = new Intent(CheckStatuses.this, CheckShortStatuses.class);
                startActivity(toCheckShort);
            }
        });
    }
}