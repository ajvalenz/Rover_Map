package com.example.rover_map;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.rover_map.databinding.ActivityMapsBinding;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Double lat, lng;
    float zoomLevel = 16.0f;
    private Button save;
    private Button send;
    private ArrayList<String> position = new ArrayList<>();
    private ArrayList<UTM> utA;
    private ArrayList<MarkerOptions> markers = new ArrayList<>();
    private String data ="";
    private String[] separated ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        save = findViewById(R.id.button1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getMarkersArray(lat, lng);
                getMarkersArray2(markers);
            }
        });
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertToUTM(position);
            }
        });


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
    }
        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
// crear una lista de tipo   marker options que almacene todos y
            LatLng sunnyK = new LatLng(37.374539, 126.667549);
            String x = sunnyK.latitude + "," + sunnyK.longitude;
            position.add(x);
            mMap.addMarker(new MarkerOptions().position(sunnyK).title("Marker in SUNNYK"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sunnyK, zoomLevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(point);
                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(point.latitude + " : " + point.longitude);

                    // Placing a marker on the touched position
                    googleMap.addMarker(markerOptions);

                    markers.add(markerOptions);

                    lat = markerOptions.getPosition().latitude;
                    lng = markerOptions.getPosition().longitude;
                }
            });
        }
    void getMarkersArray2 ( ArrayList<MarkerOptions> marker){
        for( MarkerOptions m : marker){
            String pos = m.getPosition().latitude + "," +m.getPosition().longitude;
            position.add(pos);
            Toast toast = Toast.makeText(MapsActivity.this, "Marker location saved", Toast.LENGTH_SHORT);
            toast.show();
        }
        System.out.println(position.size());
    }
        void getMarkersArray (Double lat, Double lng){
            String pos = lat.toString() + "," + lng.toString();
            position.add(pos);
            Toast toast = Toast.makeText(MapsActivity.this, "Marker location saved", Toast.LENGTH_SHORT);
            toast.show();
            for (String x : position
            ) {
                System.out.println(x);
            }
            //System.out.println(position.size());
        }
        void convertToUTM (ArrayList < String > list) {
        try{
            utA = new ArrayList<>();
            for (String position : list) {
                separated = position.split(",");
                Double lat2 = Double.valueOf(separated[0]);
                Double lng2 = Double.valueOf(separated[1]);
                WGS84 loc = new WGS84(lat2, lng2);
                UTM ut = new UTM(loc);
                utA.add(ut);
            }
            sendData(utA);
        } catch (Exception e){
            Toast msg = Toast.makeText(MapsActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
            msg.show();
        }

    }

    String sendData(ArrayList<UTM> u) {

        try {
            data ="";
            for (UTM utm : u) {
                data += utm.toString() + ",";
            }
            System.out.println(data);
            Toast toast = Toast.makeText(MapsActivity.this, "Sending Data", Toast.LENGTH_SHORT);
            toast.show();
            Toast toast2 = Toast.makeText(MapsActivity.this, "Done", Toast.LENGTH_SHORT);
            toast2.show();
        }
        catch (Exception e){
            Toast msg = Toast.makeText(MapsActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
            msg.show();
        }
            return data;
        }
    }


