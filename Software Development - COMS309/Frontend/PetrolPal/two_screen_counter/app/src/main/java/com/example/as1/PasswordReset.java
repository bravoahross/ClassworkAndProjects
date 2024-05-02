package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordReset extends AppCompatActivity {


    EditText cPassword;
    EditText nPassword1;
    EditText nPassword2;
    Button changeButton;
    TextView messages;

    String oldPass;
    Long userID;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        changeButton = findViewById(R.id.changeButton);
        cPassword = findViewById(R.id.oldPassword);
        nPassword1 = findViewById(R.id.newPassword1);
        nPassword2 = findViewById(R.id.newPassword2);
        messages = findViewById(R.id.messageCenter);
        intent = getIntent();
        if(intent != null){
            oldPass = intent.getStringExtra("password");
            userID = intent.getLongExtra("userID",-1);
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-031.class.las.iastate.edu:8080/account/" + String.valueOf(userID) + "/"; // Update later
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cPassword.getText().toString().equals(oldPass)){
                    if(nPassword1.getText().toString().equals(nPassword2.getText().toString())){
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url + nPassword1.getText().toString(), null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error){
                                messages.setText(url + nPassword1.getText().toString());
                            }
                        });
                        queue.add(jsonObjectRequest);
                    }
                    else{
                        messages.setText("Passwords do not match.");
                    }
                }
                else{
                    messages.setText("Old password is not correct.");
                }
            }
        });
    }
}