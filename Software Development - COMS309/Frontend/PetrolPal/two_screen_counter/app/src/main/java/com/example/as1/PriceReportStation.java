package com.example.as1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PriceReportStation extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView reportsList;

    ArrayList<ReportModal> reportModalArrayList;
    ReportAdapter adapter;
    AlertDialog.Builder builder;
    RequestQueue queue;

    String stationIDRecieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_report_station);
        reportsList = findViewById(R.id.reportsID);
        reportModalArrayList = new ArrayList<>();
        builder = new AlertDialog.Builder(this);
        Intent intent = getIntent();
        if(intent != null){
            stationIDRecieved = intent.getStringExtra("stationID");
        }

        queue = Volley.newRequestQueue(PriceReportStation.this);
        String url = "http://coms-309-031.class.las.iastate.edu:8080/report/station/" + stationIDRecieved;
        //String url = "https://5f70336d-2ffb-464d-aba9-d5075611de02.mock.pstmn.io/reports/2";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length()+1; i++){
                    try{
                        JSONObject responseObj = response.getJSONObject(i);

                        String username = "Report #" + String.valueOf(i + 1);
                        String stationPrice = responseObj.getString("price");
                        String time = responseObj.getString("date");
                        reportModalArrayList.add(new ReportModal(username,stationPrice,time));
                        // initializing our adapter class.
                        adapter = new ReportAdapter(reportModalArrayList, PriceReportStation.this, PriceReportStation.this);

                        // adding layout manager
                        // to our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(PriceReportStation.this);
                        reportsList.setHasFixedSize(true);

                        // setting layout manager
                        // to our recycler view.
                        reportsList.setLayoutManager(manager);

                        // setting adapter to
                        // our recycler view.
                        reportsList.setAdapter(adapter);

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(PriceReportStation.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(int position) {
        String usernamePos = reportModalArrayList.get(position).getUsername();
        String pricePos = reportModalArrayList.get(position).getStationPrice();
        JSONObject update = new JSONObject();
        //Toast.makeText(GasMainForm.this, userReceived, Toast.LENGTH_LONG).show();
        try{
            update.put("price", pricePos);
        } catch (Exception e){
            e.printStackTrace();
        }
        builder.setTitle("Accept Price Report?")
                .setMessage("Accept report from: " + usernamePos + "?\nNew Price: " + pricePos)
                .setCancelable(true)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://coms-309-031.class.las.iastate.edu:8080/station/put/price/" + stationIDRecieved.toString() + "/" + pricePos; // Whatever the put url for updating a gas price is.
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, update, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JsonObjectRequest deleteList = new JsonObjectRequest(Request.Method.DELETE, "http://coms-309-031.class.las.iastate.edu:8080/report/delete/" + stationIDRecieved.toString(), null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(PriceReportStation.this, "Report List Cleared.", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error){
                                        //Toast.makeText(PriceReportStation.this, "Failed to Delete the list.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                queue.add(deleteList);
                                Intent intent = new Intent();
                                intent.putExtra("price", pricePos);
                                intent.putExtra("status", "changed");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Toast.makeText(PriceReportStation.this, "Failed to Update Price", Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(jsonObjectRequest);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }
}