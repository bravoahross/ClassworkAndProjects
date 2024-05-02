package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Activity used by Gas Stations to read messages sent to them, and reply to them. Uses Websocket protocols for communications.
 * @author Noah Ross
 */
public class GasChatPage extends AppCompatActivity implements WebSocketListener{

//    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private String BASE_URL = "ws://coms-309-031.class.las.iastate.edu:8080/chat/";
    private String username;

    private Button backBtn, sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);

        Intent intent = getIntent();
        if(intent != null) {
            username = intent.getStringExtra("username");
            userID = intent.getLongExtra("userID", 0);
        }

        /* initialize UI elements */
        backBtn = (Button) findViewById(R.id.backBtn);
        sendBtn = (Button) findViewById(R.id.bt2);
        msgEtx = (EditText) findViewById(R.id.et2);
        msgTv = (TextView) findViewById(R.id.tx1);
        //msgEtx.setText(username);

        String serverUrl = BASE_URL + username;

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(GasChatPage.this);

        /* back button listener */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GasChatPage.this, GasMain.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                WebSocketManager.getInstance().removeWebSocketListener();
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(intent);
            }
        });

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {

                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
    }

    /**
     * Take the current chunk of messages and append the newest message to the bottom. Replace the view on the screen.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
        });
    }

    /**
     * Display a message when the chat closes for any reason.
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}
}
