package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import java.util.Calendar;
import java.util.Date;
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


public class PriceReportUser extends AppCompatActivity {
    String userReceived;
    String stationIDRecieved;

    Long userID;

    Button submit;
    EditText field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_report_user);
        this.setTitle("Report New Price");
        Intent intent = getIntent();
        if(intent != null){
            userReceived = intent.getStringExtra("username");
            stationIDRecieved = intent.getStringExtra("stationID");
            userID = intent.getLongExtra("userID", -1);
        }
        submit = findViewById(R.id.submitReport);
        field = findViewById(R.id.newPrice);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postRequest();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }
    private void postRequest() throws JSONException {


        String requestURL ="http://coms-309-031.class.las.iastate.edu:8080/report/post/" + userID.toString() + "/" + stationIDRecieved.toString();


        RequestQueue queue = Volley.newRequestQueue(PriceReportUser.this);

        // Convert input to JSONObject
        JSONObject report = new JSONObject();
        //Toast.makeText(GasMainForm.this, userReceived, Toast.LENGTH_LONG).show();
        try{
            report.put("date", Calendar.getInstance().getTime());
            report.put("price", field.getText());
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestURL, report,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PriceReportUser.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                        Log.d("Volley Response", String.valueOf(error));
                    }
                }
        );
        queue.add(request); // send request
    }
}