package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    String url = "https://ramcaf.000webhostapp.com/ramcaf/signup.php";
    String globalEmail;
    String globalPass;
    String name, password1, password2, clickSignup, radStudent, radStaff;
    char atSymbol = 64;
    boolean tc = false;
    boolean student, staff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final TextInputEditText txtName = findViewById(R.id.txtNameInput);
        final TextInputEditText txtEmailSignUp = findViewById(R.id.txtInputEmail);
        final TextInputEditText txtPassSignUp = findViewById(R.id.txtPassInput);
        final TextInputEditText txtPassSignup2 = findViewById(R.id.txtPassInput2);
        final Button btnSignUp = findViewById(R.id.btnSignUp);
        RadioButton studentRad = findViewById(R.id.radStudent);
        RadioButton staffRad = findViewById(R.id.radStaff);
        CheckBox readTC = findViewById(R.id.checkBoxTC);
        Button btnToTC = findViewById(R.id.btnToTC);


        studentRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStudent = atSymbol + "student.apc.edu.ph";
                student = true;
            }
        });

        staffRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStaff = atSymbol + "apc.edu.ph";
                staff = true;
            }
        });

        btnToTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTermsAndConds = new Intent(Signup.this, TermsAndConditions.class);
                startActivity(toTermsAndConds);
            }
        });

        readTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tc = true;
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password1 = txtPassSignUp.getText().toString();
                password2 = txtPassSignup2.getText().toString();
                name = txtName.getText().toString();
                globalEmail = txtEmailSignUp.getText().toString();
                globalPass = password1;
                clickSignup = "signup";

                if(student == true){
                    globalEmail = globalEmail + radStudent;
                }else if(staff == true){
                    globalEmail = globalEmail + radStaff;
                }else{
                    Toast.makeText(Signup.this, "Select from email radio button.", Toast.LENGTH_SHORT).show();
                }

                if(tc == true){
                    if(!(name.isEmpty()||globalEmail.isEmpty()||globalPass.isEmpty())){
                        if(!(password1.equals(password2))) {
                            Toast.makeText(Signup.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                        }else {
                            globalPass = password1;
                            signup(name, globalEmail, globalPass, clickSignup);
                            show();
                            Toast.makeText(Signup.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Signup.this, "Incomplete details.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Signup.this, "You have not agreed to the Terms and Conditions.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void show(){

        Intent toUserInterface = new Intent(Signup.this, MainActivity.class);

        toUserInterface.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toUserInterface);
        finish();

    }//show()

    public void signup(final String nameSign, final String emailSign ,final String passSign, final String clickSignup){

        RequestQueue requestQueue = Volley.newRequestQueue(Signup.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Hitesh"," "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh",""+error);
                Toast.makeText(Signup.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap = new HashMap<> ();
                stringMap.put("user_name", nameSign);
                stringMap.put("email", emailSign);
                stringMap.put("password", passSign);
                stringMap.put("signup", clickSignup);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    } //signup
}
