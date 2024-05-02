package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Creates a recycler view list of reviews given a station id for the user to view.
 * GET request gets an Array of JSON Objects and assigns the rating and descriptions
 * to be viewed in a carousel.
 * @author Noah Ross
 */
public class ReviewList extends AppCompatActivity {

    RecyclerView reviewRV;

    ProgressBar progressBar;

    ArrayList<ReviewModal> reviewModalArrayList;

    String nameRecieved;
    String stationID;

    String username;

    ReviewAdapter adapter;

    Button Back;

    Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewlist);

        Back = findViewById(R.id.backBtn);

        reviewRV = findViewById(R.id.list_of_reviews);
        progressBar = findViewById(R.id.progressBar);
        reviewModalArrayList = new ArrayList<>();
        Intent intent = getIntent();
        nameRecieved = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        userID = intent.getLongExtra("userID", 0);
        stationID = intent.getStringExtra("stationID");
        this.setTitle("All Reviews");

        RequestQueue queue = Volley.newRequestQueue(ReviewList.this);
//        String url = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/ReviewList";

        // Actual Server URL
        String url = "http://coms-309-031.class.las.iastate.edu:8080/reviews/station/" + stationID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                reviewRV.setVisibility(View.VISIBLE);
                for(int i = 0; i < response.length(); i++){
                    try{
                        JSONObject responseObj = response.getJSONObject(i);
                        String description = responseObj.getString("description");
                        Double rating = responseObj.getDouble("rating");
                        reviewModalArrayList.add(new ReviewModal(description, rating));

                        // initializing our adapter class.
                        adapter = new ReviewAdapter(reviewModalArrayList, ReviewList.this);

//                        // adding layout manager to our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(ReviewList.this);
                        reviewRV.setHasFixedSize(true);
//
//                        // setting layout manager to our recycler view.
                        reviewRV.setLayoutManager(manager);
//
//                        // setting adapter to our recycler view.
                        reviewRV.setAdapter(adapter);

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(ReviewList.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ReviewList.this, UserMain.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }
}
