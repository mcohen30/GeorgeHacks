package medical_arsenal.fitband;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private Button emergencyButton;
    private Button falseButton;
//    private EditText inputHR;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private TextView locationText;
    private TextView accText;
    private Vibrator vibrator;
    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat df2 = new SimpleDateFormat("HH:mm:ss");

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.VIBRATE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
//        inputHR = findViewById(R.id.inputText);
        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        locationText = findViewById(R.id.locationText);
        accText = findViewById(R.id.accText);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];

//                    if (Integer.valueOf(inputHR.getText().toString()) < 50 || Integer.valueOf(inputHR.getText().toString()) > 150) {
//                        vibrator.vibrate(3000);
//                    }
                    if (x > 30 || y > 30 || z > 30) {


//
                        ringtone.play();
                        JSONObject realtimeData = new JSONObject();

                        try {
                            realtimeData.put("type", "REALTIME");
                            realtimeData.put("date", df1.format(Calendar.getInstance().getTime()));
                            realtimeData.put("time", df2.format(Calendar.getInstance().getTime()));
                            realtimeData.put("latitude", "38.90");
                            realtimeData.put("longitude", "-77.05");
                            realtimeData.put("x", String.valueOf(x));
                            realtimeData.put("y", String.valueOf(y));
                            realtimeData.put("z", String.valueOf(z));
                            realtimeData.put("heart_rate", "56");
                            SendDeviceDetails sdd = new SendDeviceDetails();
                            sdd.execute("https://georgehacks-199112.appspot.com/realtime", realtimeData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("x = ");
                    sb.append(x);
                    sb.append("\ny = ");
                    sb.append(y);
                    sb.append("\nz = ");
                    sb.append(z);
                    sb.append("\n");
                    sb.append(df1.format(Calendar.getInstance().getTime()));
                    sb.append("\n");
                    sb.append(df2.format(Calendar.getInstance().getTime()));

                    accText.setText(sb.toString());

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            locationText.setText("null");
        }
        updateLocation(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
                checkPermission();
                updateLocation(locationManager.getLastKnownLocation(s));
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        emergencyButton = findViewById(R.id.button1);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                updateLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                ringtone.play();
//                vibrator.vibrate(3000);
//                accText.setText("111");
            }
        });
        falseButton = findViewById(R.id.button2);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                inputHR.setText("");
                ringtone.stop();
            }
        });
    }

    private void checkPermission() {
        for (int i = 0; i < PERMISSIONS.length; i++) {
            if (ContextCompat.checkSelfPermission(this,
                    PERMISSIONS[i])
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        PERMISSIONS[i])) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{PERMISSIONS[i]},
                            i);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void updateLocation(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Real Time GPS:\n");
            sb.append("longitude:");
            sb.append(location.getLongitude());
            sb.append("\nlatitude:");
            sb.append(location.getLatitude());
            sb.append("\nheight:");
            sb.append(location.getAltitude());
            sb.append("\nspeed：");
            sb.append(location.getSpeed());
            sb.append("\ndirection：");
            sb.append(location.getBearing());
            locationText.setText(sb.toString());
        } else {
//            locationText.setText("no");
        }
    }





    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }


}
