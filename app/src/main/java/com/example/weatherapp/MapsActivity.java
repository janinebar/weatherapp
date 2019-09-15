package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


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



/***
 * Main Maps activity
 */
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

        TextView input_location = (TextView) findViewById(R.id.mapSearch);

    }

    public void changeMap() {

        // Add a marker in Sydney and move the camera
        LatLng newLocation = new LatLng(30, -97);
        mMap.addMarker(new MarkerOptions().position(newLocation).title("Inputted location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
    }

    private class submitThread extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params){
            return "string";
        }

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

            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=600+West+26th+Street+Austin,+TX&key="+api;
            String url3 = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway+Mountain+View,+CA&key="+api;
            String url2 = "https://api.myjson.com/bins/par8d";


            // Volley Request Stuff
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());

            // Request a string response from the provided URL.
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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
                                lat = getLat(location);
                                lng = getLng(location);
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

    }
//    public void mapSearch(View view){
//
//        // get location string
//        final TextView input_location = (TextView) findViewById(R.id.mapSearch);
//        //final String input = input_location.getText().toString();
//        final String input = "Austin,+TX";
//
//        //Debug
//        final TextView debug_input = (TextView) findViewById(R.id.textView);
//        debug_json = (TextView) findViewById(R.id.textView4);
//        debug_input.setText(input);
//        System.out.println("----------Button clicked-----");
//
//        //turn into lat/lng
//
//        String api = "AIzaSyDVrJPxByXeTsFiB7MJaq46_qDM8a8t1DQ";
//
//        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=600+West+26th+Street+Austin,+TX&key="+api;
//        String url3 = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway+Mountain+View,+CA&key="+api;
//        String url2 = "https://api.myjson.com/bins/par8d";
//
//
//        // Volley Request Stuff
//        queue = Volley.newRequestQueue(this);
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest jsonRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    //JSON Parser
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        // Process the JSON
//                        try{
//
//                            JSONArray jsonArray = response.getJSONArray("results"); // the overall results
//                            debug_json.setText("worked");
//
//                            // Get lat and lng from json results
//                            System.out.println(response.toString());
//
//                            JSONObject object = jsonArray.getJSONObject(0);
//                            JSONObject location = object.getJSONObject("geometry").getJSONObject("location");
//                            lat = getLat(location);
//                            setLat(lat);
//                            lng = getLng(location);
//                            System.out.println("------------- INFO " + lat + " " + lng);
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    //If error with request
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        debug_json.setText("error");
//                        Log.e("VOLLEY", error.getMessage());
//
//                    }
//                });
//
//        queue.add(jsonRequest);
//
//    }

    public void weather(){

        String darkSky_url = "https://api.darksky.net/forecast/8802f65c465beff5374ad280f538f4ec/30.2907524,-97.74349509999999";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, darkSky_url, null, new Response.Listener<JSONObject>() {

                    //JSON Parser
                    @Override
                    public void onResponse(JSONObject response) {

                        // Process the JSON
                        try{

//                            JSONArray jsonArray = response.getJSONArray("results"); // the overall results
//                            debug_json.setText("worked");

                            // Get lat and lng from json results
                            System.out.println(response.toString());

                            JSONObject object = response.getJSONObject("currently");
//                            JSONObject location = object.getJSONObject("currently");
                            double temp = object.getDouble("temperature");
                            double humidity = object.getDouble("humidity");
                            double precip = object.getDouble("precipProbability");
                            double windsp = object.getDouble("windSpeed");
//                            System.out.println(temp);
//                            System.out.println(humidity);
//                            System.out.println(precip);
//                            System.out.println(windsp);

                            //set textboxes
                            final TextView tempBox = (TextView) findViewById(R.id.temp);
                            tempBox.setText("Temperature: " + String.valueOf(temp));
                            final TextView precipBox = (TextView) findViewById(R.id.precip);
                            precipBox.setText("Precipitation: " + String.valueOf(precip));
                            final TextView humidBox = (TextView) findViewById(R.id.humidity);
                            humidBox.setText("Humidity: " + String.valueOf(humidity));
                            final TextView windBox = (TextView) findViewById(R.id.windspeed);
                            windBox.setText("Temperature: " + String.valueOf(windsp));

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
