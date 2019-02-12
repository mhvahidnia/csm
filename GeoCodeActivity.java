package com.example.emertat.crowdsourceproj;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

public class GeocodeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_LATLONG6 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocode);
    }



    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }



    public void onSearchClick(View view) {
        try {
            EditText AddressText= (EditText) findViewById(R.id.editText2);
            LatLng pointcode= getLocationFromAddress(this,AddressText.getText().toString());
            EditText editText4 = (EditText) findViewById(R.id.editText4);
            editText4.setText(String.valueOf(pointcode.latitude));
            EditText editText6 = (EditText) findViewById(R.id.editText6);
            editText6.setText(String.valueOf(pointcode.longitude));
        } catch(Exception ex) {
           ex.printStackTrace();
        }

    }


    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        EditText editTextLat = (EditText) findViewById(R.id.editText4);
        EditText editTextLong = (EditText) findViewById(R.id.editText6);
        String messageLat = editTextLat.getText().toString();
        String messageLong = editTextLong.getText().toString();
        String messageLatLong = String.valueOf(messageLat)+ "," +String.valueOf(messageLong);
        intent.putExtra(EXTRA_MESSAGE_LATLONG6, messageLatLong);
        startActivity(intent);

    }
}
