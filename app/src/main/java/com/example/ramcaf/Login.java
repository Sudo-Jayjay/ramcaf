package com.example.ramcaf;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    String url = "https://ramcaf.000webhostapp.com/ramcaf/login.php";
    String email, password;

    Button btnSignUp;
    Button txtAdminLogin, txtStaffLogin;
    ProgressBar progressBar;
    String globalName;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvitivy_login);

        btnSignUp = findViewById(R.id.btnLSignup);
        final Button btnLogIn = findViewById(R.id.btnLogin);
        final TextInputEditText txtEmail = findViewById(R.id.txtInputEmailLoginAdmin);
        final TextInputEditText txtPassword = findViewById(R.id.txtInputPassLoginAdmin);
        txtAdminLogin = findViewById(R.id.txtToAdminLogin);
        txtStaffLogin = findViewById(R.id.txtToStaffLogin);
        progressBar = findViewById(R.id.progressBar);

        txtAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdminLogin = new Intent(Login.this, AdminLogin.class);
                startActivity(toAdminLogin);
            }
        });

        txtStaffLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toStaffLogin = new Intent(Login.this, StaffLogin.class);
                startActivity(toStaffLogin);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(Login.this,Signup.class);
                startActivity(toSignUp);
            }
        });


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if(!(email.isEmpty() )&& !(password.isEmpty())){
                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String responseResult = response.substring(0,7);
                            String response_UserID = response.substring(8);

                            if (responseResult.equals("success")) {
                                progressBar.setVisibility(View.GONE);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userid", response_UserID);
                                editor.commit();

                                Intent toMain = new Intent(Login.this, MainActivity.class);
                                startActivity(toMain);
                                finish();
                            } else if (responseResult.equals("failure")) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Incorrect login credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("email", email);
                            data.put("password", password);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);
                }else {
                    Toast.makeText(Login.this, "Incomplete fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
