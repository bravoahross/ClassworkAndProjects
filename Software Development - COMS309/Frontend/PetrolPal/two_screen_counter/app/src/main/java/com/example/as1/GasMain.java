package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Button;

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
 * First Activity seen by a GasStation User when signed in. Displays a list of all stations owned by the gas station user. Has the ability to create a new station from here or to tap on one of the stations that they already own and edit it.
 * @author Noah Ross, Alex Brown
 */
public class GasMain extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView stationRV;

    ProgressBar progressBar;

    ArrayList<StationModal> stationModalArrayList;

    String nameRecieved;

    String username;

    long userID;

    StationAdapter adapter;

    Button createStation;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_main);

        createStation = findViewById(R.id.create);
        logout = findViewById(R.id.logout);

        stationRV = findViewById(R.id.owned);
        progressBar = findViewById(R.id.progressBar);
        stationModalArrayList = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null) {
            nameRecieved = intent.getStringExtra("name");
            username = intent.getStringExtra("username");
            userID = intent.getLongExtra("userID", 0);
        }
        this.setTitle("All Stations");

        RequestQueue queue = Volley.newRequestQueue(GasMain.this);
        //String url = "https://5f70336d-2ffb-464d-aba9-d5075611de02.mock.pstmn.io/station";
        String url = "http://coms-309-031.class.las.iastate.edu:8080/station/all"; // Change link to work for just the ones owned by user once we get that from backend.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                stationRV.setVisibility(View.VISIBLE);
                for(int i = 0; i < response.length(); i++){
                    try{
                        JSONObject responseObj = response.getJSONObject(i);

                        String stationName = responseObj.getString("stationName");
                        long stationID = responseObj.getLong("gasId");


                        String stationPrice = responseObj.getString("price");
                        String stationAddress = responseObj.getString("address");
                        String stationDescription = responseObj.getString("description");
                        if(responseObj.getString("username").equals(""+userID)){
                            stationModalArrayList.add(new StationModal(stationName,stationID,stationPrice,stationAddress,stationDescription));
                            // initializing our adapter class.
                            adapter = new StationAdapter(stationModalArrayList, GasMain.this, GasMain.this);

                            // adding layout manager
                            // to our recycler view.
                            LinearLayoutManager manager = new LinearLayoutManager(GasMain.this);
                            stationRV.setHasFixedSize(true);

                            // setting layout manager
                            // to our recycler view.
                            stationRV.setLayoutManager(manager);

                            // setting adapter to
                            // our recycler view.
                            stationRV.setAdapter(adapter);

                        }

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(GasMain.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);

        createStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasMain.this, GasMainForm.class);
                intent.putExtra("type", "create");
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasMain.this, StartupScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(GasMain.this, GasMainForm.class);
        long temp = stationModalArrayList.get(position).getStationID();
        String real = Long.toString(temp);
        intent.putExtra("type", "update");
        intent.putExtra("stationName", stationModalArrayList.get(position).getStationName());
        intent.putExtra("stationID", real);
        intent.putExtra("stationAddress", stationModalArrayList.get(position).getStationAddress());
        intent.putExtra("stationPrice", stationModalArrayList.get(position).getStationPrice());
        intent.putExtra("stationDescription", stationModalArrayList.get(position).getStationDescription());
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}