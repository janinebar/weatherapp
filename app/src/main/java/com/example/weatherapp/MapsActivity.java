package com.example.weatherapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Anna
     * @param View
     * @return
     */
    public LatLng mapSearch(View view){
        LatLng result = new LatLng(0,0);

        // get location string
        final TextView input_location = (TextView) findViewById(R.id.mapSearch);
        //final String input = input_location.getText().toString();
        final String input = "Austin,+TX";

        //Debug
        final TextView debug_input = (TextView) findViewById(R.id.textView);
        final TextView debug_json = (TextView) findViewById(R.id.textView4);
        debug_input.setText(input);
        System.out.println("----------Button clicked-----");

        //turn into lat/lng

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=Austin,+TX&key=AIzaSyDJiHyUAOjgFFWLfcrNSp14zr_dFmrZYnI";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    //JSON Parser
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON
                        try{

                            System.out.println(response.toString());
                            debug_json.setText("Geocode successful");

                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject object = response.getJSONObject(i);
                                System.out.println("-------------" + object.toString());

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    //If error with request
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        debug_json.setText("url request error");

                    }
                });




        //put into map


        return result;
    }

}
