package com.example.ramcaf;

import androidx.appcompat.app.AppCompatActivity;

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

public class StaffLogin extends AppCompatActivity {

    String url = "https://ramcaf.000webhostapp.com/ramcaf/staffLogin.php";
    String staff_email, staff_password;
    ProgressBar staffProgressBar;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        final Button btnLogInStaff = findViewById(R.id.btnLoginStaff);
        final TextInputEditText txtStaffEmail = findViewById(R.id.txtInputEmailLoginStaff);
        final TextInputEditText txtStaffPassword = findViewById(R.id.txtInputPassLoginStaff);
        staffProgressBar = findViewById(R.id.staffProgress);



        btnLogInStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staff_email = txtStaffEmail.getText().toString();
                staff_password = txtStaffPassword.getText().toString();

                if(!(staff_email.isEmpty() )&& !(staff_password.isEmpty())){
                    staffProgressBar.setVisibility(View.VISIBLE);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String responseResult = response.substring(0,7);
                            String response_StaffID = response.substring(8);

                            if (responseResult.equals("success")) {
                                staffProgressBar.setVisibility(View.GONE);

                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("staffid", response_StaffID);
                                editor.commit();

                                Intent toHome = new Intent(StaffLogin.this, StaffHome.class);
                                startActivity(toHome);
                                finish();
                            } else if (responseResult.equals("failure")) {
                                staffProgressBar.setVisibility(View.GONE);
                                Toast.makeText(StaffLogin.this, "Incorrect login credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(StaffLogin.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("staff_email", staff_email);
                            data.put("staff_password", staff_password);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);
                }else {
                    Toast.makeText(StaffLogin.this, "Incomplete fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
