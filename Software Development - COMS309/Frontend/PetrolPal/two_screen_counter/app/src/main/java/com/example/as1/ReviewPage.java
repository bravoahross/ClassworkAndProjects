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

import java.util.ArrayList;

/**
 * Allows users to input their rating as a double and their description as a string to post it publicly.
 * @author Noah Ross
 */
public class ReviewPage extends AppCompatActivity {
    Button Submit;
    Button Back;

    Double rating;
    EditText description;

    TextView stationName;

    String stationID;
    String username;
    String stationNameText;

    Long userID;

    Double respond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewpage);

        // Text
        stationName = findViewById(R.id.station);

        // Text inputs
        description = findViewById(R.id.inputDescription);

        // Button Assignments
        Submit = findViewById(R.id.submitBtn);
        Back = findViewById(R.id.backBtn);

        Intent intent = getIntent();
        if(intent != null) {
            username = intent.getStringExtra("username");
            userID = intent.getLongExtra("userID", 0);
            stationID = intent.getStringExtra("stationID");
            stationNameText = intent.getStringExtra("stationName");
        }

        Spinner spinner = findViewById(R.id.rateSpinner);
        Double[] methods = new Double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_spinner_dropdown_item, methods);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rating = (Double) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rating = 0.0;
            }
        });


        //stationNameChat = intent.getStringExtra("stationName");

//        Submit.setText(userID.toString());
//        Back.setText(stationID);
        stationName.setText(stationNameText);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    postRequest(stationID, String.valueOf(userID));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ReviewPage.this, UserMain.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * POST request sends rating (double) and description (string)
     * @param gas gas station id the user is reviewing
     * @param user userID of the user reviewing
     * @throws JSONException
     */
    private void postRequest(String gas, String user) throws JSONException {

        // Actual Server URL
        String requestURL = "http://coms-309-031.class.las.iastate.edu:8080/review/post/" + gas + "/" + user;

        RequestQueue queue = Volley.newRequestQueue(ReviewPage.this);

        // Convert input to JSONObject
        JSONObject signUp = new JSONObject();

        try{
            signUp.put("rating", rating);
            signUp.put("description", description.getText());
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestURL, signUp,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            respond = response.getDouble("rating");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewPage.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                        Log.d("Volley Response", String.valueOf(error));
                    }
                }
        );

        queue.add(request); // send request
    }
}
