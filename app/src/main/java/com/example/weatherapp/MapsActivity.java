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
import com.android.volley.toolbox.HttpResponse;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/***
 * Main Maps activity
 */
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
        LatLng sydney = new LatLng(-32, 47);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /***
     * Submit button functionality
     * @param view
     */
    public void submitButton(View view) {

        submitThread findLocation = new submitThread();
        findLocation.execute("");

    }

    private class submitThread extends AsyncTask<String, Void, String> {

        String result = "";
        double lat = 0;
        double lng = 0;

        //Location search bar
        final TextView searchBar = (TextView) findViewById(R.id.mapSearch);

        // Weather textBoxes
        final TextView tempBox = (TextView) findViewById(R.id.temp);
        final TextView precipBox = (TextView) findViewById(R.id.precip);
        final TextView humidBox = (TextView) findViewById(R.id.humidity);
        final TextView windBox = (TextView) findViewById(R.id.windspeed);

        // initialize weather data
        double temp = 0;
        double humidity = 0;
        double precip = 0;
        double windsp = 0;

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            String api = "AIzaSyDVrJPxByXeTsFiB7MJaq46_qDM8a8t1DQ";
            BufferedReader reader = null;
            String coordinates = "";

            String originalInput = searchBar.getText().toString();
            String[] inputArr = originalInput.split(" ");
            String input = "";
            for(int i = 0; i < inputArr.length; i++){
                input += inputArr[i] + "+";
            }



            try {

                // Sends Geocode API request
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+input+"&key=" + api);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Gets JSON Data
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                while ((result = reader.readLine()) != null) {
                    buffer.append(result + "\n");
                    Log.d("Response: ", result); // line by line printing
                }

                //Parses JSON data
                try {
                    JSONObject response = new JSONObject(buffer.toString()); //from doInBackground
                    JSONArray jsonArray = response.getJSONArray("results"); // the overall results

                    // Get lat and lng from json results
                    System.out.println(response.toString()); // one line response

                    JSONObject object = jsonArray.getJSONObject(0);
                    JSONObject location = object.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    System.out.println("------------- INFO " + lat + " " + lng);
                    coordinates = String.valueOf(lat) + " " + String.valueOf(lng);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Closes Geocode API connections
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



            // Weather Info
            String darkSky_url = "https://api.darksky.net/forecast/8802f65c465beff5374ad280f538f4ec/" + lat + "," + lng;
            HttpURLConnection connection2 = null;
            BufferedReader reader2 = null;


            try {

                // Does Weather API Call
                URL url = new URL(darkSky_url);
                connection2 = (HttpURLConnection) url.openConnection();
                connection2.connect();

                // Gets Weather JSON Data
                InputStream stream = connection2.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer2 = new StringBuffer();

                while ((result = reader.readLine()) != null) {
                    buffer2.append(result + "\n");
                    Log.d("Response: ", result); // line by line printing
                }

                // Get lat and lng from json results
                System.out.println(buffer2.toString());

                // Parses Weather JSON Data
                JSONObject response = new JSONObject(buffer2.toString());
                JSONObject object = response.getJSONObject("currently");
                temp = object.getDouble("temperature");
                humidity = object.getDouble("humidity");
                precip = object.getDouble("precipProbability");
                windsp = object.getDouble("windSpeed");


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Closes weather connections
            finally {
                if (connection2 != null) {
                    connection2.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return coordinates;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("--- done executing");
            String lat = result.split(" ")[0];
            String lng = result.split(" ")[1];

            // add marker to map
            LatLng newLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(newLocation).title("Inputted location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

            //set textboxes
            tempBox.setText("Temperature: " + String.valueOf(temp));
            precipBox.setText("Precipitation: " + String.valueOf(precip));
            humidBox.setText("Humidity: " + String.valueOf(humidity));
            windBox.setText("Temperature: " + String.valueOf(windsp));
        }



    }
}
