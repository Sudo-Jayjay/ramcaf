package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;

public class TermsAndConditions extends AppCompatActivity {

    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);


        done = findViewById(R.id.btnDone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnToSignUp = new Intent(TermsAndConditions.this, Signup.class);
                startActivity(returnToSignUp);
            }
        });
    }
}