package com.example.weatherapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
    private RequestQueue queue;
    private TextView debug_json;
    private double lat;
    private double lng;

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
        LatLng sydney = new LatLng(-32, 47);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void submitButton(View view){
        mapSearch(view);
        System.out.println(this.lat + " " + this.lng);
    }

    public void changeMap(LatLng location) {

        // Add a marker in Sydney and move the camera
        LatLng newLocation = location;
        mMap.addMarker(new MarkerOptions().position(newLocation).title("Inputted location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
    }

    /**
     * Anna
     * @param view
     * @return
     */
    public void mapSearch(View view){

        // get location string
        final TextView input_location = (TextView) findViewById(R.id.mapSearch);
        //final String input = input_location.getText().toString();
        final String input = "Austin,+TX";

        //Debug
        final TextView debug_input = (TextView) findViewById(R.id.textView);
        debug_json = (TextView) findViewById(R.id.textView4);
        debug_input.setText(input);
        System.out.println("----------Button clicked-----");

        //turn into lat/lng

        String api = "AIzaSyDVrJPxByXeTsFiB7MJaq46_qDM8a8t1DQ";

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=600+West+26&key="+api;
        String url3 = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway+Mountain+View,+CA&key=AIzaSyDJiHyUAOjgFFWLfcrNSp14zr_dFmrZYnI";
        String url2 = "https://api.myjson.com/bins/par8d";


        // Volley Request Stuff
        queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    //JSON Parser
                    @Override
                    public void onResponse(JSONObject response) {

                        // Process the JSON
                        try{

                            JSONArray jsonArray = response.getJSONArray("results"); // the overall results
                            debug_json.setText("worked");

                            // Get lat and lng from json results
                            System.out.println(response.toString());

                            JSONObject object = jsonArray.getJSONObject(0);
                            JSONObject location = object.getJSONObject("geometry").getJSONObject("location");
                            double lat = getLat(location);
                            double lng = getLng(location);
                            System.out.println("------------- INFO " + lat + " " + lng);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    //If error with request
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        debug_json.setText("error");
                        Log.e("VOLLEY", error.getMessage());

                    }
                });

        queue.add(jsonRequest);

    }

    // Returns latitude
    public double getLat(JSONObject location){
        double latitude = 0;
        try {
             latitude = location.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.lat = latitude;
        return this.lat;
    }

    // Returns longitude
    public double getLng(JSONObject location){
        double longitude = 0;
        try {
            longitude = location.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.lng = longitude;
        return this.lng;
    }

}
