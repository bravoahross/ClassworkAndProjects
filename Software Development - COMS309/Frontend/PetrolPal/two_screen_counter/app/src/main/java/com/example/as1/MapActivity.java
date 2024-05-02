package com.example.as1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * Activity all for displaying a map with pins showing the 20 closest gas stations. Allows users to continue into their respective FullStationUserView if they choose.
 * @author Alex Brown
 */
public class MapActivity extends AppCompatActivity {

    BottomNavigationView nav;
    MapView map;

    boolean isPermissionGranted;

    MyLocationNewOverlay locationOverlay;

    double lon;

    Context ctx;
    ArrayList<OverlayItem> items;
    ArrayList<StationModal> stationsForMap;

    String username;
    String password;
    Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        Intent intent = getIntent();
        if(intent != null) {
            username = intent.getStringExtra("username");
            userID = intent.getLongExtra("userID", 0);
            password = intent.getStringExtra("password");
        }
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        lon = -93.651288;
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(42.027268,-93.651288);
        mapController.setCenter(startPoint);
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        locationOverlay = new MyLocationNewOverlay(map);
        locationOverlay.enableFollowLocation();
        map.getOverlayManager().add(locationOverlay);
        items = new ArrayList<OverlayItem>();
        stationsForMap = new ArrayList<StationModal>();
        String url = "http://coms-309-031.class.las.iastate.edu:8080/station/42.027268/-93.651288";
        RequestQueue queue = Volley.newRequestQueue(MapActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try{
                        JSONObject responseObj = response.getJSONObject(i);

                        String stationName = responseObj.getString("stationName");
                        int stationID = responseObj.getInt("gasId");
                        String stationPrice = responseObj.getString("price");
                        String stationAddress = responseObj.getString("address");
                        String stationDescription = responseObj.getString("description");
                        if(responseObj.getDouble("lat") != 0.0 || responseObj.getDouble("lon") != 0.0) {
                            stationsForMap.add(new StationModal(stationName, stationID, stationPrice, stationAddress, stationDescription));
                            items.add(new OverlayItem(stationName, stationPrice, new GeoPoint(responseObj.getDouble("lat"), responseObj.getDouble("lon"))));
                            //Toast.makeText(MapActivity.this, Double.toString(responseObj.getDouble("lat")), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                            @Override
                            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                //do something

                                return true;
                            }
                            @Override
                            public boolean onItemLongPress(final int index, final OverlayItem item) {
                                Intent intent = new Intent(MapActivity.this, FullStationViewUser.class);
                                intent.putExtra("stationName", stationsForMap.get(index).getStationName());
                                intent.putExtra("stationID", stationsForMap.get(index).getStationID());
                                intent.putExtra("stationAddress", stationsForMap.get(index).getStationAddress());
                                intent.putExtra("stationPrice", stationsForMap.get(index).getStationPrice());
                                intent.putExtra("stationDescription", stationsForMap.get(index).getStationDescription());
                                intent.putExtra("username", "username");
                                startActivity(intent);
                                return false;
                            }
                        }, ctx);
                mOverlay.setFocusItemsOnTap(true);

                map.getOverlays().add(mOverlay);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                items.add(new OverlayItem("Error", "Hmmm", new GeoPoint(42.024800,-93.658010)));
            }
        });
        queue.add(jsonArrayRequest);


        nav = findViewById(R.id.bottomNavView_Bar);
        //nav.setItemBackgroundResource(R.drawable.menubackground);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nb_list:
                        intent = new Intent(MapActivity.this, UserMain.class);
                        intent.putExtra("username", username);
                        intent.putExtra( "userID", userID);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        break;
                    case R.id.nb_map:
                        break;
                    case R.id.nb_profile:
                        intent = new Intent(MapActivity.this, ProfileActivity.class);
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

    public void onResume(){
        super.onResume();
        Configuration.getInstance().load(this,PreferenceManager.getDefaultSharedPreferences(this));
        locationOverlay.enableMyLocation();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
        locationOverlay.disableMyLocation();
    }


}