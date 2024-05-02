package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Activity that allows GasStation users to either create a new station or update one of their existing ones. Does this based on intent passed into it.
 * @author Noah Ross, Alex Brown
 */
public class GasMainForm extends AppCompatActivity {

    long userReceived;
    String status;
    Button Update;
    Button Logout;
    Button Chat;
    Button Back;
    Button dod;
    Button reports;
    EditText name;
    EditText price;
    EditText address;
    EditText description;

    String purpose;

    String stationNameR;
    String idRecieved;

    String descriptionR;
    String stationNameChat;
    String stationID;
    String addressR;
    String priceR;

    long userID;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasmainform);

        // Text inputs
        this.name = (EditText) this.findViewById(R.id.inputName);
        this.price = (EditText) this.findViewById(R.id.inputPrice);
        this.address = (EditText) this.findViewById(R.id.inputAddress);
        this.description = (EditText) this.findViewById(R.id.inputDescription);

        // Button Assignments
        Update = findViewById(R.id.updateBtn);
        Logout = findViewById(R.id.logoutBtn);
        Chat = findViewById(R.id.chatBtn);
        Back = findViewById(R.id.backBtn);
        dod = findViewById(R.id.dodButtonSt);
        reports = findViewById(R.id.priceReports);

        Chat.setVisibility(View.GONE);
        dod.setVisibility(View.GONE);


        this.setTitle("Create A Station");
        Intent intent = getIntent();
        purpose = intent.getStringExtra("type");
        if(purpose.equals("update")){
            name.setText(intent.getStringExtra("stationName"));
            price.setText(intent.getStringExtra("stationPrice"));
            address.setText(intent.getStringExtra("stationAddress"));
            description.setText(intent.getStringExtra("stationDescription"));
            idRecieved = intent.getStringExtra("stationID");
            Chat.setVisibility(View.VISIBLE);
            dod.setVisibility(View.VISIBLE);
            this.setTitle("Update Station: " + intent.getStringExtra("stationName"));
        }
        stationNameChat = intent.getStringExtra("stationName");
        stationID = intent.getStringExtra("stationID");
        userID = intent.getLongExtra("userID", 0);
        username = intent.getStringExtra("username");

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();
                userReceived = intent.getLongExtra("userID", 0);


                //Toast.makeText(GasMainForm.this, userReceived, Toast.LENGTH_LONG).show();
                try {
                    putRequest(purpose, intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasMainForm.this, StartupScreen.class);
                startActivity(intent);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasMainForm.this, GasMain.class);
                intent.putExtra("name", "Test");
                intent.putExtra("username", username);
                intent.putExtra( "userID", userID);
                startActivity(intent);
            }
        });

        dod.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(GasMainForm.this, DodActivity.class);
                intent.putExtra("stationID", stationID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasMainForm.this, GasChatPage.class);
                intent.putExtra("username", stationID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GasMainForm.this, PriceReportStation.class);
                intent.putExtra("stationID", stationID);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("status") == "changed"){
                price.setText(intent.getStringExtra("price"));
            }
        }
    }

    /**
     * Method for sending gas station data to the server. If the type is "create" we make a post request, but if it is "update" we make a put.
     * @param type
     * @param intent
     * @throws JSONException
     */
    private void putRequest(String type, Intent intent) throws JSONException {

        // Postman
//        String requestURL = "https://51b8477f-8136-41b8-9098-0bd60099c71c.mock.pstmn.io/station/put";

        // Actual Server
        int verb = Request.Method.POST;
        String requestURL = null;
        if(type.equals("create")){
            requestURL = "http://coms-309-031.class.las.iastate.edu:8080/station/post";
            verb = Request.Method.POST;
        } else if (type.equals("update")) {
            requestURL = "http://coms-309-031.class.las.iastate.edu:8080/station/put/" + idRecieved;
            //Toast.makeText(GasMainForm.this, requestURL, Toast.LENGTH_LONG).show();
            verb = Request.Method.PUT;
        }


        RequestQueue queue = Volley.newRequestQueue(GasMainForm.this);

        // Convert input to JSONObject
        JSONObject signUp = new JSONObject();
        //Toast.makeText(GasMainForm.this, userReceived, Toast.LENGTH_LONG).show();
        try{
            signUp.put("username", userReceived);
            signUp.put("stationName", name.getText());
            signUp.put("price", price.getText());
            signUp.put("address", address.getText());
            signUp.put("description", description.getText());
            signUp.put("status", "success");
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(verb, requestURL, signUp,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley Response", response.toString());
                    try {
                        status = response.getString("status");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if(status.equals("success")) {
                        Update.setText("success");
                    }
                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GasMainForm.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                    Log.d("Volley Response", String.valueOf(error));
                }
            }
        );
        queue.add(request); // send request
    }
}