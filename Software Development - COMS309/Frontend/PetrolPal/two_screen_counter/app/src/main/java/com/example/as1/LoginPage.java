package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Activity used by all classes of user in order to sign in. Takes username and password input and only allows the user to get to the next activity upon successful login as shown by the return from the server.
 * @author Alex Brown
 */
public class LoginPage extends AppCompatActivity {

    Button login;

    Button signup;

    EditText usernameField;

    EditText passwordField;

    TextView label;



    String name;
    String username;
    String status;

    String paramed;

    String userClass;

    Intent intent;

    Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.toSU);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        label = findViewById(R.id.label);
        intent = getIntent();

        String url = "http://coms-309-031.class.las.iastate.edu:8080/account/login/"; // Server
        //String url = "https://5f70336d-2ffb-464d-aba9-d5075611de02.mock.pstmn.io/account/login/"; // Postman
        RequestQueue queue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                label.setText("Working...");
                //Map<String, String> params = new HashMap();
                //params.put("username", usernameField.getText().toString());
                //params.put("password", passwordField.getText().toString());

                //JSONObject parameters = new JSONObject(params);
                username = usernameField.getText().toString();
                //paramed = url + "?username=" + username + "&password=" + passwordField.getText().toString();
                paramed = url + username + "/" + passwordField.getText().toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, paramed, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            name = response.getString("firstname");
                            status = response.getString("status");
                            userClass = response.getString("accessLevel");
                            userID = response.getLong("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(status.equals("true")){
                            label.setText("Welcome " + name);
                            if(userClass.equals("gas") || userClass.equals("2")){
                                intent = new Intent(LoginPage.this, GasMain.class);
                            }
                            else if (userClass.equals("user") || userClass.equals("1")){
                                intent = new Intent(LoginPage.this, UserMain.class);
                            }
                            else if (userClass.equals("admin")){
                                intent = new Intent(LoginPage.this, AdminPage.class);
                            }
                            else{
                                //Do nothing, shouldn't be possible anyway
                            }
                            intent.putExtra("name", name);
                            intent.putExtra("class", userClass);
                            intent.putExtra("username", username);
                            intent.putExtra("userID", userID);
                            intent.putExtra("password", passwordField.getText().toString());
                            startActivity(intent);
                        }
                        else if (status.equals("wrong")) {
                            label.setText("Password and username do not match");
                        }
                        else if (status.equals("missing")) {
                            label.setText("No such username exists");
                        }
                        else{
                            label.setText("Invalid Login.\nPlease try again...");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        label.setText(error.toString());
                    }
                });
                //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);
                //Intent intent = new Intent(LoginPage.this, StartupScreen.class);
                //startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(LoginPage.this, SignupPage.class);
                startActivity(intent);
            }
        });
    }


}