package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Provides a page for Admin users to remove stations and/or users for general cleanup and moderation on the app.
 *
 * @author Noah Ross
 */
public class AdminPage extends AppCompatActivity {

    Button deleteUser;

    Button deleteStation;
    Button logout;

    EditText usernameField;

    EditText stationID;

    String name;
    String username;
    String success;
    String status;

    String paramed;

    String userClass;

    Intent intent;

    /**
     * Initialize all variables that need connected to Views in the activity and set button listeners. Create Request queue.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);
        deleteUser = findViewById(R.id.deleteUser);
        deleteStation = findViewById(R.id.deleteStation);
        logout = findViewById(R.id.logoutBtn);
        usernameField = findViewById(R.id.username);
        stationID = findViewById(R.id.stationID);
//        label = findViewById(R.id.label);
        intent = getIntent();

        //String url = "http://coms-309-031.class.las.iastate.edu:8080/userDelete/"; // Server
        //String url = "https://5f70336d-2ffb-464d-aba9-d5075611de02.mock.pstmn.io/account/login/"; // Postman
        RequestQueue queue = Volley.newRequestQueue(this);


        deleteUser.setOnClickListener(new View.OnClickListener() {
            /**
             * Send a post request to delete the user from the database.
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                String typeIn = "userDelete";

                try {
                    postRequest(typeIn, String.valueOf(usernameField.getText()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        deleteStation.setOnClickListener(new View.OnClickListener() {
            /**
             * Send a post request to delete the station from the database.
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                String typeIn = "StationDelete";
                try {
                    postRequest(typeIn, String.valueOf(stationID.getText()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            /**
             * Return user to the main splash page. Bring no intent containing user variables from beforehand.
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                intent = new Intent(AdminPage.this, StartupScreen.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Post request functionality for deleting users and/or stations from the database.
     * @param type Either "userDelete" or "StationDelete" used to adjust the url in the post request
     * @param deleteToken The ID of the station or user being delted. Added to the url in the post request
     * @throws JSONException
     */
    private void postRequest(String type, String deleteToken) throws JSONException {

        // Postman test URL
//         String requestURL = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/signup";

        // Actual Server URL
        String requestURL = "http://coms-309-031.class.las.iastate.edu:8080/" + type + "/" + deleteToken;

        RequestQueue queue = Volley.newRequestQueue(AdminPage.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, requestURL, null,
            new Response.Listener<JSONObject>() {
                /**
                 * When getting a response, pull the status out of it to make sure it worked.
                 * @param response
                 */
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley Response", response.toString());
                    if (type.equals("userDelete")) {
                        try {
                            status = response.getString("username");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (type.equals("StationDelete")) {
                        try {
                            status = response.getString("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdminPage.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                    Log.d("Volley Response", String.valueOf(error));
                }
            }
        );
        queue.add(request); // Add request
    }
}
