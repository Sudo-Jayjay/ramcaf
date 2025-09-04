package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class AdminLogin extends AppCompatActivity {

    String url = "https://ramcaf.000webhostapp.com/ramcaf/adminLogin.php";
    String admin_email, admin_password, admin_name;
    ProgressBar adminProgressBar;
    String globalAdminName;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        final Button btnLogInAdmin = findViewById(R.id.btnLoginAdmin);
        final TextInputEditText txtAdminEmail = findViewById(R.id.txtInputEmailLoginAdmin);
        final TextInputEditText txtAdminPassword = findViewById(R.id.txtInputPassLoginAdmin);
        adminProgressBar = findViewById(R.id.adminProgress);


        btnLogInAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_email = txtAdminEmail.getText().toString();
                admin_password = txtAdminPassword.getText().toString();

                if(!(admin_email.isEmpty() )&& !(admin_password.isEmpty())){
                    adminProgressBar.setVisibility(View.VISIBLE);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String responseResult = response.substring(0,7);
                            String response_AdminID = response.substring(8);

                            if (responseResult.equals("success")) {
                                adminProgressBar.setVisibility(View.GONE);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("adminid", response_AdminID);
                                editor.commit();

                                Intent toAdminHome = new Intent(AdminLogin.this, AdminHome.class);
                                startActivity(toAdminHome);
                                finish();
                            } else if (responseResult.equals("failure")) {
                                adminProgressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminLogin.this, "Incorrect login credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AdminLogin.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("admin_email", admin_email);
                            data.put("admin_password", admin_password);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);
                }else {
                    Toast.makeText(AdminLogin.this, "Incomplete fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
