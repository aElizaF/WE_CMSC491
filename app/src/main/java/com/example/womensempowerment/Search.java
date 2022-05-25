package com.example.womensempowerment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class Search extends FragmentActivity implements OnMapReadyCallback {

    // Location Elements
    private LocationManager locationManager;
    public String bestProvider;
    private LocationListener locationListener;
    private Object LatLng;
    Location currentLocation;
    public Criteria criteria;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    // Map Elements
    private GoogleMap mMap;

    // Sensor Elements
    private SensorManager mSensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private Float azimut;

    private Location getLastKnownLocation () {
        Location l= null;
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                l = locationManager.getLastKnownLocation(provider);
            }
            if (l == null){
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Map updates
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Fetching Location
        fusedLocationProviderClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        //private void locationMethod(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("TAG", "getLastLocation Success");
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Search.this::onMapReady);
                } else {
                    Log.d("TAG", "Location not successful");
                }
            }
        });

        // Adding in Sensor Data to add to GPS
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    mGravity = sensorEvent.values;
                if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                    mGeomagnetic = sensorEvent.values;
                if (mGravity != null && mGeomagnetic != null) {
                    float R[] = new float[9];
                    float I[] = new float[9];
                    boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                    if (success) {
                        float orientation[] = new float[3];
                        SensorManager.getOrientation(R, orientation);
                        azimut = orientation[0];
                        float degrees = (float) Math.toDegrees(azimut);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public static boolean isLocationEnabled(Context context) {
        return true;
    }

    private void fetchLocation() {
        if (isLocationEnabled(Search.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        }
        // Checks permissions
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } //else {
        //locationManager.requestLocationUpdates(bestProvider, 1000, 0, LocationListener, this);
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
        locationManager.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("TAG", "Google Map is Loaded!");
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng center = new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(center).title(center.latitude + ":" + center.longitude);
        mMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(center));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 18));
        googleMap.addMarker(markerOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    public void loadNearPlaces(double latitude, double longitude) {
        // TODO expand services by adding more search parameters

        mMap.clear();
        Intent i = getIntent();
        String hospital;
        String type = "hospital";

        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(getLastKnownLocation());
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyClIdXEpyLtaI4d2ITx7x5vvXZdBmPOSsI");

    }
}