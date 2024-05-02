package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * Allows users to signup provided their input matches the given parameters and the username is unique.
 * POST request sends username, password, first and last name, and user type which creates a profile.
 * @author Noah Ross
 */
public class SignupPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String usernameBackend;
    String accessLevel;
    String spinnerVal;
    String status;

    Boolean testVal;
    Boolean testVal2;
    Boolean testVal3;

    Intent intent;
    Intent intent2;
    Intent intent3;

    Button loginBtn;
    Button SignUpBtn;

    EditText username;
    EditText password;
    EditText firstname;
    EditText lastname;

    Long userID;

    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        // Text inputs
        this.username = (EditText) this.findViewById(R.id.inputUsername);
        this.password = (EditText) this.findViewById(R.id.inputPassword);
        this.firstname = (EditText) this.findViewById(R.id.inputFirstName);
        this.lastname = (EditText) this.findViewById(R.id.inputLastName);

        //Error output
        this.error = (TextView) this.findViewById(R.id.Error);

        // Buttons
        loginBtn = findViewById(R.id.backBtn);
        SignUpBtn = findViewById(R.id.toMainBtn);

        //TODO: figure out spinner input for volley

        // method spinner
        Spinner spinner = findViewById(R.id.spinner);
        String[] methods = new String[]{"User", "Gas Station", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, methods);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerVal = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerVal = "User";
            }
        });


        // Log in page button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(SignupPage.this, GasMain.class);
                intent2 = new Intent(SignupPage.this, UserMain.class);
                intent3 = new Intent(SignupPage.this, AdminPage.class);

                if (username.getText().toString().length() < 8) {
                    error.setText("Error: Username is less than 8 characters");
                } else if (password.getText().toString().length() < 8) {
                    error.setText("Error: Password is less than 8 characters");
                } else if (firstname.getText().toString().length() > 20) {
                    error.setText("Error: First Name is more than 20 characters");
                } else if (lastname.getText().toString().length() > 20) {
                    error.setText("Error: Last Name is more than 20 characters");
                } else {
                    error.setText("");
                    try {
                        postRequest();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void postRequest() throws JSONException {

        // Postman test URL
//         String requestURL = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/signup";

        // Actual Server URL
        String requestURL = "http://coms-309-031.class.las.iastate.edu:8080/account";

        RequestQueue queue = Volley.newRequestQueue(SignupPage.this);

        // Convert input to JSONObject
        JSONObject signUp = new JSONObject();

        String accessString = (String) spinnerVal;
        String tempStatus = "good";
        if ("User".equals(accessString)) {
            accessString = "user";
        } else if ("Gas Station".equals(accessString)) {
            accessString = "gas";
        } else if ("Admin".equals(accessString)) {
            accessString = "admin";
        }

        try{
            signUp.put("username", username.getText());
            signUp.put("password", password.getText());
            signUp.put("firstname", firstname.getText());
            signUp.put("lastname", lastname.getText());
            signUp.put("accessLevel", accessString);
            signUp.put("status", tempStatus);
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestURL, signUp,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            accessLevel = response.getString("accessLevel");
                            usernameBackend = response.getString("username");
                            userID = response.getLong("id");
                            status = response.getString("status");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if (status.equals("good")) {
                            testVal = "gas".equals(accessLevel);
                            testVal2 = "user".equals(accessLevel);
                            testVal3 = "admin".equals(accessLevel);
                            intent.putExtra("username", username.getText().toString());
                            intent2.putExtra("username", username.getText().toString());
                            intent3.putExtra("username", username.getText().toString());
                            intent.putExtra("userID", userID);
                            intent2.putExtra("userID", userID);
                            intent3.putExtra("userID", userID);
                            if (testVal) {
                                startActivity(intent);
                            } else if (testVal2) {
                                intent2.putExtra("password", password.getText().toString());
                                startActivity(intent2);
                            } else if (testVal3) {
                                startActivity(intent3);
                            } else {
                            }
                        }
                        else if (status.equals("duplicate")) {
                            error.setText("Username already exists. \n Try a different one");
                        }
                        else {
                            error.setText("Error try again");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupPage.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                        Log.d("Volley Response", String.valueOf(error));
                    }
                }
        );

        queue.add(request); // send request
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}