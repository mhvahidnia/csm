package com.example.emertat.crowdsourceproj;

import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class LocActivity extends AppCompatActivity implements SensorEventListener {

    double azimut;
    double vertical;
    double x0,y0,x,y,E,N;
    double Lo_final,La_final;

    private LocationManager loc;

    protected ImageButton buttonPhoto;

    public static final String EXTRA_MESSAGE_LATLONG4 = "";

    private SensorManager mSensorManager;
    Sensor accelermeter;
    Sensor magnetometer;



    private Camera mCamera;
    private CameraPreview mPreview;

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String messageFull=intent.getStringExtra(MainActivity.EXTRA_MESSAGE_LATLONG0);
        String[] separated = messageFull.split(",");
        String messageLat = separated[0];
        String messageLong = separated[1];

        // Capture the layout's TextView and set the string as its text
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(messageLat);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(messageLong);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelermeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);





        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        setCameraDisplayOrientation(this, cameraId,mCamera);


        // set Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        if (params.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-30, -30, 30, 30);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            params.setMeteringAreas(meteringAreas);
        }

        mCamera.setParameters(params);

    }



    public class ButtonClickHandler implements View.OnClickListener
    {
        public void onClick( View view ){
            Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
            startCameraActivity();
        }
    }


    protected void startCameraActivity() {
        Log.i("MakeMachine", "startCameraActivity()" );

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        //intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, 0 );
    }






    public void onClick(View view) {

        TextView editTextLat = (TextView) findViewById(R.id.textView1);
        TextView editTextLong = (TextView) findViewById(R.id.textView2);

        E = Double.parseDouble(editTextLong.getText().toString());
        N = Double.parseDouble(editTextLat.getText().toString());

        Deg2Utm D2U = new Deg2Utm();
        double[] Es_No = D2U.Deg2Utm(N,E);
        x0 = Es_No[0];
        y0 = Es_No[1];
        int zon = D2U.Zone;
        char leter = D2U.Letter;

        TextView textview =(TextView) findViewById(R.id.textView9);
        textview.setText(String.valueOf(x0));

        textview =(TextView)findViewById(R.id.textView10);
        textview.setText(String.valueOf(y0));

        textview =(TextView)findViewById(R.id.textView11);
        textview.setText(String.valueOf(zon));

        textview =(TextView)findViewById(R.id.textView12);
        textview.setText(String.valueOf(leter));



        double az = azimut;
        double v1 = vertical;
        double v2;
        v2 = ((Math.PI)/2)-v1;
        EditText ed = (EditText)findViewById(R.id.editText);
        float s = (float)Float.parseFloat(ed.getText().toString());
        float d;
        d = (float)(s/Math.cos(v2));
        float l;
        l = (float)Math.sqrt((d*d)-(s*s));

        if(az<0) {
            az = Math.PI + (Math.PI + az);
        }
        x = x0+l*Math.sin(az);
        y = y0+l*Math.cos(az);

        textview =(TextView)findViewById(R.id.textView14);
        textview.setText(String.valueOf(x));

        textview =(TextView)findViewById(R.id.textView15);
        textview.setText(String.valueOf(y));

        Utm2Deg U2D = new Utm2Deg();
        double[] Lo_La_final = U2D.Utm2Deg(zon+" "+leter+" "+x+" "+y);
        Lo_final = Lo_La_final[0];
        La_final = Lo_La_final[1];

        textview =(TextView)findViewById(R.id.textView16);
        textview.setText(String.valueOf(Lo_final));

        textview =(TextView)findViewById(R.id.textView17);
        textview.setText(String.valueOf(La_final));

    }



    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }



    public void Map(View view){
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+N+","+E+"?q="+La_final+","+Lo_final+"&z=16"));
        startActivity(i);
    }


    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, accelermeter,SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);


    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    };


    protected void onStop(){
        super.onStop();
    }





    float[] mGravity;
    float[] mGeomagnetic;
    public void onSensorChanged(SensorEvent event) {


        // TODO Auto-generated method stub
        if (event.sensor.getType()== Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null){
            float RR[] =new float[9];
            float II[] =new float[9];
            boolean success = SensorManager.getRotationMatrix(RR, II, mGravity, mGeomagnetic);
            if (success){
                float orientation[] =new float[3];
                SensorManager.getOrientation(RR, orientation);
                azimut = orientation[0];
                vertical= orientation[1];

                //---------------------------------------------------------------
                TextView textviewA =(TextView)findViewById(R.id.textView28);
                textviewA.setText(String.valueOf(azimut));

                TextView textviewV =(TextView)findViewById(R.id.textView31);
                textviewV.setText(String.valueOf(vertical));

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }



    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        TextView editTextLat = (TextView) findViewById(R.id.textView17);
        TextView editTextLong = (TextView) findViewById(R.id.textView16);
        String messageLat = editTextLat.getText().toString();
        String messageLong = editTextLong.getText().toString();
        String messageLatLong = String.valueOf(messageLat)+ "," +String.valueOf(messageLong);
        intent.putExtra(EXTRA_MESSAGE_LATLONG4, messageLatLong);
        startActivity(intent);
    }
}
