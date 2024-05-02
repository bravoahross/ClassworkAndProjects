package com.example.as1;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class to manage and connect to a chat
 * Connects to websocket and references Websocket manager and listener as needed
 * @author Noah Ross
 */
public class NewChat extends AppCompatActivity {

    Button backBtn, sendBtn;
    String username;
    String stationID;
    EditText e2;
    TextView t1;

    Long userID;

    private WebSocketClient cc;

    /**
     * Sets up the chat and ties all components to their respective id's in the corresponding xml file
     * Sets up intent to get any necessary extra values from the previous page
     * @param savedInstanceState  the previous state that the app was in, prior to advancing to this page
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchat);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        e2 = (EditText) findViewById(R.id.et2);
        t1 = (TextView) findViewById(R.id.tx1);

        Intent intent = getIntent();
        if(intent != null) {
            username = intent.getStringExtra("username");
            stationID = intent.getStringExtra("stationID");
            userID = intent.getLongExtra("userID", 0);
        }

        Draft[] drafts = {
                new Draft_6455()
        };

        String w = "ws://coms-309-031.class.las.iastate.edu:8080/chat/" + username;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), (Draft) drafts[0]) {
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String s = t1.getText().toString();
                    t1.setText(s + "\nServer:" + message);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();


        /**
         * Back Button listener that goes back to the Usermain page.
         * Puts: username
         */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewChat.this, UserMain.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                cc.close();
                startActivity(intent);
            }
        });

        /**
         * Send Button listener that sends the message to the websocket
         */
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "@" + stationID + " " + e2.getText().toString();
                try {
                    cc.send(msg);
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });
    }
}
