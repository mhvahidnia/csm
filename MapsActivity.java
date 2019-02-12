package com.example.emertat.crowdsourceproj;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap mMap;
    public static final String EXTRA_MESSAGE_LATLONG2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();
            String messageFull = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_LATLONG);
            String[] separated = messageFull.split(",");
            String messageLat = separated[0];
            String messageLong = separated[1];

            // Capture the layout's TextView and set the string as its text
            TextView textView4 = (TextView) findViewById(R.id.textView4);
            textView4.setText(messageLat);
            TextView textView5 = (TextView) findViewById(R.id.textView5);
            textView5.setText(messageLong);

            mMap = googleMap;

           
            LatLng InitPos = new LatLng(Double.parseDouble(messageLat), Double.parseDouble(messageLong));
            mMap.addMarker(new MarkerOptions().position(InitPos).title("Initial Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(InitPos));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    // TODO Auto-generated method stub
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(point).title("Modified Position"));
                   // mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                    TextView textView4 = (TextView) findViewById(R.id.textView4);
                    textView4.setText(String.valueOf(point.latitude));
                    TextView textView5 = (TextView) findViewById(R.id.textView5);
                    textView5.setText(String.valueOf(point.longitude));
                }
            });


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
    {
        try {
            mMap.setOnMapClickListener(this);
            //mMap.setOnMapLongClickListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapClick(LatLng point) {
       // mTapTextView.setText("tapped, point=" + point);
        try {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(point).title("Modified Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            TextView textView4 = (TextView) findViewById(R.id.textView4);
            textView4.setText(String.valueOf(point.latitude));
            TextView textView5 = (TextView) findViewById(R.id.textView5);
            textView5.setText(String.valueOf(point.longitude));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        TextView editTextLat = (TextView) findViewById(R.id.textView4);
        TextView editTextLong = (TextView) findViewById(R.id.textView5);
        String messageLat = editTextLat.getText().toString();
        String messageLong = editTextLong.getText().toString();
        String messageLatLong = String.valueOf(messageLat)+ "," +String.valueOf(messageLong);
        intent.putExtra(EXTRA_MESSAGE_LATLONG2, messageLatLong);
        startActivity(intent);
    }
}
