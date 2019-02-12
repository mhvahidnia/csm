package com.example.emertat.crowdsourceproj;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Random;
import 	java.net.HttpURLConnection;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Timer;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.EditText;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import android.view.View.OnClickListener;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;



public class MainActivity extends AppCompatActivity {

    Button add;
    TextView display;
    int counter;
    protected ImageButton buttonPhoto;
    protected ImageView _image;
    protected TextView _field;
    protected String _path;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN	= "photo_taken";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String _sendLat;
    protected String _sendLong;
    protected String _sendDescription;
    protected String _sendImage;
    protected int i1;

    Timer timer;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    double x,y;
    LocationListener locationListenerGps;
    LocationListener locationListenerNetwork;

    private Button button;

    public static final String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE_LONG = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE_LATLONG = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE_LATLONG0 = "com.example.myfirstapp.MESSAGE";

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "00.00.00.00";
    // http://ourhost.com/

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "username";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="password";


    static final int REQUEST_IMAGE_CAPTURE = 0;

    static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());




        int min = 1;
        int max = 30000;
        Random r = new Random();
        i1 = r.nextInt(max - min + 1) + min;


        try {
            Intent intent = getIntent();
            String messageFull=intent.getStringExtra(MapsActivity.EXTRA_MESSAGE_LATLONG2);
            if (messageFull !="" ){

                String[] separated = messageFull.split(",");
                String messageLat = separated[0];
                String messageLong = separated[1];
                // Capture the layout's TextView and set the string as its text
                TextView editText1 = (TextView) findViewById(R.id.editText1);
                editText1.setText(messageLat);
                TextView editText01 = (TextView) findViewById(R.id.editText01);
                editText01.setText(messageLong);
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }



        // IF THE RESULT OF MODIFIED POSITIONING CAME BACK
        //:::::::
        try {
            Intent intent = getIntent();
            String messageFull=intent.getStringExtra(LocActivity.EXTRA_MESSAGE_LATLONG4);
            if (messageFull !="" ){

                String[] separated = messageFull.split(",");
                String messageLat = separated[0];
                String messageLong = separated[1];
                // Capture the layout's TextView and set the string as its text
                TextView editText1 = (TextView) findViewById(R.id.editText1);
                editText1.setText(messageLat);
                TextView editText01 = (TextView) findViewById(R.id.editText01);
                editText01.setText(messageLong);
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }


        // IF THE RESULT OF MODIFIED POSITIONING CAME BACK
        //:::::::
        try {
            Intent intent = getIntent();
            String messageFull=intent.getStringExtra(GeocodeActivity.EXTRA_MESSAGE_LATLONG6);
            if (messageFull !="" ){

                String[] separated = messageFull.split(",");
                String messageLat = separated[0];
                String messageLong = separated[1];
                // Capture the layout's TextView and set the string as its text
                TextView editText1 = (TextView) findViewById(R.id.editText1);
                editText1.setText(messageLat);
                TextView editText01 = (TextView) findViewById(R.id.editText01);
                editText01.setText(messageLong);
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }





        ImageButton buttonMap=(ImageButton) findViewById(R.id.imageBtnMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                EditText EditTextLat=(EditText) findViewById(R.id.editText1);
                EditText EditTextLong=(EditText) findViewById(R.id.editText01);
                _sendLat=EditTextLat.getText().toString();
                _sendLong=EditTextLong.getText().toString();
                EditText EditTextDesc=(EditText) findViewById(R.id.editText3);
                _sendDescription=EditTextDesc.getText().toString();
                _sendImage = "http://ourhost.com/img" + Integer.toString(i1) + ".jpg";

                /********** Pick file from sdcard *******/
                File f = new File(Environment.getExternalStorageDirectory() + "/images/img" + Integer.toString(i1) + ".jpg");




                new Connection().execute();


                try {


               
                    new Thread(new Runnable() {
                        public void run(){
                            try {
                                String URLstring = "http://ourhost.com/file.aspx?lat=" + _sendLat + "&long=" + _sendLong + "&desc=" + _sendDescription + "&image=" + _sendImage;
                  
                                String URLstringEncoded = URLstring.replace(" ", "%20");
 
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(URLstringEncoded);

                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                Log.d("Http Response:", httpResponse.toString());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();


                } catch (Exception e) {
                    //System.out.println("Error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });



        // View Online map
        final Context context = this;
        button = (Button) findViewById(R.id.buttonUrl);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, WebViewActivity.class);
                startActivity(intent);
            }

        });



        //Start GPS position
        ImageButton buttonGPS=(ImageButton) findViewById(R.id.imageBtnGPS);


        buttonGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);




                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                locationListener=new LocationListener() {

                    @Override
                    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onProviderEnabled(String arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onProviderDisabled(String arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        String LatString;
                       String LongString;
                        EditText LatText;
                        EditText LongText;
                        LatText=(EditText) findViewById(R.id.editText1);
                        LongText=(EditText) findViewById(R.id.editText01);
                        if (location != null) {
                            double lat=location.getLatitude();
                            double lng=location.getLongitude();
                            LatString="" + lat;
                            LongString=""+ lng;
                        } else {
                            LatString="No Info";
                            LongString="No Info";
                        }
                        LatText.setText(LatString);
                        LongText.setText(LongString);
                    }
                };

                if (gps_enabled) {

                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                                    PERMISSION_ACCESS_COARSE_LOCATION);
                            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,0, 0, locationListener);
                        }
                    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,0, 0, locationListener);
                        // Permission is not granted
                   }
                else if (gps_enabled != true && network_enabled==true){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                                PERMISSION_ACCESS_COARSE_LOCATION);
                        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,0, 0, locationListener);

                    }
                    locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,0, 0, locationListener);
                }

                }




        });
        //End position



        // take photo and save
        _image = ( ImageView ) findViewById( R.id.imageView1 );
        _path = Environment.getExternalStorageDirectory() + "/images/img" + Integer.toString(i1) + ".jpg";
        buttonPhoto = ( ImageButton ) findViewById( R.id.imageBtnPhoto );
        buttonPhoto.setOnClickListener( new ButtonClickHandler() );


    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




    class Connection extends AsyncTask {
        private Exception exception;
        @Override
        
        protected Object doInBackground(Object... arg0) {
            FTPClient client = new FTPClient();
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try  {

               

                client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

                client.connect(FTP_HOST,21);

                client.login(FTP_USER, FTP_PASS);

                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();

                InputStream inputstr = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/images/img" + Integer.toString(i1) + ".jpg"));
                
                client.storeFile("/www.ourhost.com/"+"img"+Integer.toString(i1) + ".jpg",inputstr);

                client.logout();
                client.disconnect();

            } catch (Exception e) {
                e.printStackTrace();

                try {
                    client.logout();
                    client.disconnect();

                    //client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return null;
        }

    }



    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://ourhost.com/file.aspx"); // here is your URL path
  
                _sendDescription=_sendDescription.replace(" ", "%20");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("lat", _sendLat);
               postDataParams.put("long", _sendLong);
                postDataParams.put("desc", _sendDescription);
                postDataParams.put("image", _sendImage);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());

            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }




    public class DoUpload implements Runnable {

        @Override
        public void run() {
            FTPClient client = new FTPClient();
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try  {
                //Your code goes here

                client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                Toast.makeText(getApplicationContext(), "PRINT_WRITER", Toast.LENGTH_LONG).show();
                client.connect(FTP_HOST,21);
                Toast.makeText(getApplicationContext(), "HOST_DETECTED", Toast.LENGTH_LONG).show();
                client.login(FTP_USER, FTP_PASS);
                Toast.makeText(getApplicationContext(), "LOGIN_SUCCESSFULLY", Toast.LENGTH_LONG).show();
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();
                Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();
                InputStream inputstr = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/images/img" + Integer.toString(i1) + ".jpg"));
                client.storeFile("/www.ourhost.com/"+Integer.toString(i1) + ".jpg",inputstr);
                Toast.makeText(getApplicationContext(), "UPLOADED", Toast.LENGTH_LONG).show();
                client.logout();
                client.disconnect();
                // client.upload(f, new MyTransferListener());

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                try {
                    client.logout();
                    client.disconnect();

                    //client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        /*
         * Code you want to run on the thread goes here
         */

        }

    }

    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            //btn.setVisibility(View.GONE);
            // Transfer started
            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {

            //btn.setVisibility(View.VISIBLE);
            // Transfer completed

            Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
        }

        public void aborted() {

          //  btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            Toast.makeText(getBaseContext()," transfer aborted , please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
        }

        public void failed() {

          //  btn.setVisibility(View.VISIBLE);
            // Transfer failed
            System.out.println(" failed ..." );
        }

    }



    //------------------------------------------
    //--------OPEN MAP ACTIVITY ____________________
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MapsActivity.class);
        EditText editTextLat = (EditText) findViewById(R.id.editText1);
        EditText editTextLong = (EditText) findViewById(R.id.editText01);
        String messageLat = editTextLat.getText().toString();
        String messageLong = editTextLong.getText().toString();
        String messageLatLong = String.valueOf(messageLat)+ "," +String.valueOf(messageLong);
        intent.putExtra(EXTRA_MESSAGE_LATLONG, messageLatLong);
        startActivity(intent);
    }


    //------------------------------------------
    //--------OPEN LOC ACTIVITY ____________________
    /** Called when the user taps the Send button */
    public void goLoc(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LocActivity.class);
        EditText editTextLat = (EditText) findViewById(R.id.editText1);
        EditText editTextLong = (EditText) findViewById(R.id.editText01);
        String messageLat = editTextLat.getText().toString();
        String messageLong = editTextLong.getText().toString();
        String messageLatLong = String.valueOf(messageLat)+ "," +String.valueOf(messageLong);
        intent.putExtra(EXTRA_MESSAGE_LATLONG0, messageLatLong);
        startActivity(intent);
    }



    //------------------------------------------
    //--------OPEN GEOCODE ACTIVITY ____________________
    /** Called when the user taps the Send button */
    public void goGeocode(View view) {
        // Do something in response to button
        Intent myIntent = new Intent(this, GeocodeActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
    }


    public class ButtonClickHandler implements View.OnClickListener
    {
        public void onClick( View view ){
            Log.i("MakeMachine", "ButtonClickHandler.onClick()" );

            startCameraActivity();
        }
    }






    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()" );
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "images");
            // have the object build the directory structure, if needed.
            mediaStorageDir.mkdirs();
            _path = mediaStorageDir.getPath() + File.separator + "img"+ Integer.toString(i1) + ".jpg";

            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(new File(_path));

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );

            intent.putExtra( android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri );
           
            startActivityForResult( intent, REQUEST_IMAGE_CAPTURE );

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static File getOutputMediaFile(int i){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "images");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "img"+ Integer.toString(i) + ".jpg");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if (resultCode == RESULT_OK) {

                try {

                    //use imageUri here to access the image
                    _taken = true;
                    onPhotoTaken();
                    // here you will get the image as bitmap
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
       }

    }

    protected void onPhotoTaken()
    {
        try {
            Log.i("MakeMachine", "onPhotoTaken");

            _taken = true;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

            _image.setImageBitmap(bitmap);

            //_field.setVisibility( View.GONE );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState){
        Log.i( "MakeMachine", "onRestoreInstanceState()");
        if( savedInstanceState.getBoolean( MainActivity.PHOTO_TAKEN ) ) {
            onPhotoTaken();
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        outState.putBoolean( MainActivity.PHOTO_TAKEN, _taken );
    }



    private void updateWithNewLocation(Location location){
        String LatString;
        String LongString;
        EditText LatText;
        EditText LongText;
        LatText=(EditText) findViewById(R.id.editText1);
        LongText=(EditText) findViewById(R.id.editText01);

        if (location != null) {
            double lat=location.getLatitude();
            double lng=location.getLongitude();
            LatString="" + lat;
            LongString=""+ lng;
        } else {
            LatString="No Info";
            LongString="No Info";
        }
        LatText.setText(LatString);
        LongText.setText(LongString);
    }

 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(this, "onStart", 3).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //	Toast.makeText(this, "onRestart", 3).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //	Toast.makeText(this, "onResume", 3).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 	Toast.makeText(this, "onPause", 3).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 	Toast.makeText(this, "onStop", 3).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 	Toast.makeText(this, "onDestroy", 3).show();
    }

}
