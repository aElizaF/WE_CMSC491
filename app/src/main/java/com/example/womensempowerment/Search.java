package com.example.womensempowerment;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.fragment.app.FragmentActivity;

import com.example.womensempowerment.databinding.ActivitySearchBinding;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Search extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySearchBinding binding;
    UiSettings mapSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync();
        if (mMap != null) {
            //mMap.addMarker(new MarkerOptions().position(myLocation).title("Start"));
            mapSettings = mMap.getUiSettings();
            mapSettings.setScrollGesturesEnabled(true);
            mapSettings.setZoomControlsEnabled(true);
        }
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
/* Default code
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

 */
    }



    private static final String TAG_RESULT = "predictions";
    JSONObject json;

    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    String browserKey = "";
    // TODO add Google API Key here

    public void updateList(String place) {
        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String output = "json";
        String parameter = input + "&types=geocode&sensor=true&key=" +browserKey;
        url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output = "?" + parameter;

        JSONObject jsonObjReq = new JSONObject(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

        })

    }


}