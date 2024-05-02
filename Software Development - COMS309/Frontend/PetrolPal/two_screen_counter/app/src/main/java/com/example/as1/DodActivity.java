package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Activity used by Gas Station Users in order to update their deal of the day. Uses Websockets. Takes in intent or the stationID and connects as the proper user to the websocket.
 * @author Alex Brown
 */
public class DodActivity extends AppCompatActivity implements WebSocketListener {

    EditText entry;
    Button back;
    Button clear;
    Button set;

    Long userID;




    String stationID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dod);
        entry = findViewById(R.id.dodUpdate);
        back = findViewById(R.id.button);
        set = findViewById(R.id.setDeal);
        clear = findViewById(R.id.clearButton);

        Intent intent = getIntent();
        if(intent != null){
            stationID = intent.getStringExtra("stationID");
            userID = intent.getLongExtra("userID", 0);
        }

        WebSocketManager.getInstance().connectWebSocket("ws://coms-309-031.class.las.iastate.edu:8080/deal/" + stationID);
        WebSocketManager.getInstance().setWebSocketListener(DodActivity.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DodActivity.this, GasMain.class);
                //intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String message = entry.getText().toString();
                if(message.equals("")){
                    WebSocketManager.getInstance().sendMessage("No Deals at this time.");
                }
                else{
                    WebSocketManager.getInstance().sendMessage(entry.getText().toString());
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                entry.setText("");
            }
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    /**
     * When the websocket finds a message, set the entry field to that message.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            entry.setText(message);
        });
    }

    /**
     * Serves a message to the user that the websocket has closed, likely due to an error.
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() ->{
            entry.setText("WebSocket Closed, likely an error.");
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {}
}