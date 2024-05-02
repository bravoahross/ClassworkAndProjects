package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Activity that has all info replaced with the information of the station it is representing. Displays Name, price, address, description, deal of the day. Also has buttons to take you to other aspects of the station like its reviews or its chat. Uses websockets to display the deal of the day.
 * @author Alex Brown, Noah Ross
 */
public class FullStationViewUser extends AppCompatActivity implements WebSocketListener {

    Button BackButton;
    Button ChatButton;
    Button RateButton;
    Button ReviewsButton;
    Button FavButton;
    Button PriceReportButton;

    Boolean Favorite = true;

    TextView name;
    TextView price;
    TextView address;
    TextView description;

    TextView dod;
    TextView dodLabel;

    String nameReceived;
    String username;
    String priceReceived;
    String addressReceived;
    String descriptionReceived;
    String stationID;

    long userID;
    int idRecieved;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullstationviewuser);


        BackButton = findViewById(R.id.BackBtn);
        ChatButton = findViewById(R.id.ChatBtn);
        RateButton = findViewById(R.id.rateBtn);
        ReviewsButton = findViewById(R.id.reviewsBtn);
        FavButton = findViewById(R.id.FavBtn);
        PriceReportButton = findViewById(R.id.reportBtn);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        dod = findViewById(R.id.dod);
        dodLabel = findViewById(R.id.dodTitle);

        // ============================================================================================
        Intent intent = getIntent();
        if(intent != null){
            nameReceived = intent.getStringExtra("stationName");
            name.setText(nameReceived);
            priceReceived = intent.getStringExtra("stationPrice");
            price.setText(priceReceived);
            addressReceived = intent.getStringExtra("stationAddress");
            address.setText(addressReceived);
            descriptionReceived = intent.getStringExtra("stationDescription");
            description.setText(descriptionReceived);
            username = intent.getStringExtra("username");
            idRecieved = intent.getIntExtra("stationID", 0);
            stationID = intent.getStringExtra("stationID");
            userID = intent.getLongExtra("userID", 0);
        }
        WebSocketManager.getInstance().connectWebSocket("ws://coms-309-031.class.las.iastate.edu:8080/deal/" + stationID);
        WebSocketManager.getInstance().setWebSocketListener(FullStationViewUser.this);
        // ============================================================================================

        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/CheckFav";

        // Actual Server URL
        String url = "http://coms-309-031.class.las.iastate.edu:8080/accountInfo/get/isFav/" + String.valueOf(userID) + "/" + stationID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Favorite = response.getBoolean("val");
                }
                catch(JSONException e){
                    throw new RuntimeException(e);
                }
                if (Favorite) {
                    FavButton.setText("Remove fav");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(FullStationViewUser.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);

        // =======================================================================================================



        FavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Favorite) {
//                    String urladd = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/addFav";

                    // Actual Server URL
                    String urladd = "http://coms-309-031.class.las.iastate.edu:8080/accountInfo/" + userID + "/put/favStation/" + stationID;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urladd, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String temp = response.getString("username");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            FavButton.setText("Remove fav");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FullStationViewUser.this, "Fail to add Station..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(jsonObjectRequest);
                    Favorite = true;
                }
                else if (Favorite) {
//                    String urldel = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/delFav";

                    // Actual Server URL
                    String urldel = "http://coms-309-031.class.las.iastate.edu:8080/accountInfo/del/"+ userID + "/favStation/" + stationID;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urldel, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String temp = response.getString("username");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            FavButton.setText("Add fav");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            FavButton.setText(error.toString());
                            Toast.makeText(FullStationViewUser.this, "Fail to remove..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(jsonObjectRequest);
                    Favorite = false;
                }
            }
        });

        RateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(FullStationViewUser.this, ReviewPage.class);
                intent.putExtra("username", username);
                intent.putExtra( "userID", userID);
                intent.putExtra( "stationID", stationID);
                intent.putExtra("stationName", nameReceived);
                startActivity(intent);
            }
        });

        ReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(FullStationViewUser.this, ReviewList.class);
                intent.putExtra("username", username);
                intent.putExtra( "userID", userID);
                intent.putExtra( "stationID", stationID);
                intent.putExtra("stationName", nameReceived);
                startActivity(intent);
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(FullStationViewUser.this, UserMain.class);
                intent.putExtra("name", "Test");
                intent.putExtra("username", username);
                intent.putExtra( "userID", userID);
                startActivity(intent);
            }
        });

        ChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(FullStationViewUser.this, ChatPage.class);
                Intent intent = new Intent(FullStationViewUser.this, NewChat.class);
                intent.putExtra("username", username);
                intent.putExtra("stationID", stationID);
                intent.putExtra( "userID", userID);
                startActivity(intent);
            }
        });

        PriceReportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FullStationViewUser.this, PriceReportUser.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                intent.putExtra("stationID", stationID);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

    }


    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    /**
     * Replaces the text in the TextView dod with the most recent message for that station. Websocket is connected as if it is the station. Essentially a DM to itself.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        dod.setText(message);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}