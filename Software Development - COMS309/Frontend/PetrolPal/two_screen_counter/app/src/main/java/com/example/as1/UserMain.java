package com.example.as1;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * The main page a user will be taken to off of login/signup.
 * GET request gets list of gas stations to display in recycler view.
 * @author Alex Brown
 */
public class UserMain extends AppCompatActivity implements RecyclerViewInterface {

    TextView label;

    String nameRecieved;

    Button Logout;

    String[] stations;
    int[] ids;
    float[]  prices;
    String[] descriptions;
    String[] addresses;
    String username;

    BottomNavigationView nav;

    float cheapest = 10;

    int cheapestIndex;

    RecyclerView stationRV;
    StationAdapter adapter;
    ArrayList<StationModal> stationModalArrayList;
    ProgressBar progressBar;

    Long userID;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);

        Intent intentone = getIntent();
        if(intentone != null) {
            username = intentone.getStringExtra("username");
            userID = intentone.getLongExtra("userID", 0);
            password = intentone.getStringExtra("password");
        }

        nav = findViewById(R.id.bottomNavView_Bar);
        stationRV = findViewById(R.id.stationRV);
        progressBar = findViewById(R.id.idPB);
        stationModalArrayList = new ArrayList<>();
        Intent intent = getIntent();
        nameRecieved = intent.getStringExtra("name");
        this.setTitle("All Stations");
        Logout = findViewById(R.id.logout);
        RequestQueue queue = Volley.newRequestQueue(UserMain.this);
        //String url = "https://5f70336d-2ffb-464d-aba9-d5075611de02.mock.pstmn.io/station";
        String url = "http://coms-309-031.class.las.iastate.edu:8080/station/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                stationRV.setVisibility(View.VISIBLE);
                for(int i = 0; i < response.length()+1; i++){
                    try{
                        JSONObject responseObj = response.getJSONObject(i);

                        String stationName = responseObj.getString("stationName");
                        int stationID = responseObj.getInt("gasId");
                        String stationPrice = responseObj.getString("price");
                        String stationAddress = responseObj.getString("address");
                        String stationDescription = responseObj.getString("description");
                        stationModalArrayList.add(new StationModal(stationName,stationID,stationPrice,stationAddress,stationDescription));
                        // initializing our adapter class.
                        adapter = new StationAdapter(stationModalArrayList, UserMain.this, UserMain.this);

                        // adding layout manager
                        // to our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(UserMain.this);
                        stationRV.setHasFixedSize(true);

                        // setting layout manager
                        // to our recycler view.
                        stationRV.setLayoutManager(manager);

                        // setting adapter to
                        // our recycler view.
                        stationRV.setAdapter(adapter);

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(UserMain.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);

        //gasStations = findViewById(R.id.stationList);
        //GasStationListAdapter stationAdapter = new GasStationListAdapter(getApplicationContext(),stations,ids,prices, addresses);
        //gasStations.setAdapter(stationAdapter);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserMain.this, StartupScreen.class);
                startActivity(intent);
            }
        });
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nb_list:
                        break;
                    case R.id.nb_map:
                        intent = new Intent(UserMain.this, MapActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra( "userID", userID);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        break;
                    case R.id.nb_profile:
                        intent = new Intent(UserMain.this, ProfileActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra( "userID", userID);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(UserMain.this, FullStationViewUser.class);
        Long temp = stationModalArrayList.get(position).getStationID();
        String real = Long.toString(temp);
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