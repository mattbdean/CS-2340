package com.github.alphabet26.ui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.github.alphabet26.R;
import com.github.alphabet26.model.Shelter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    /**
     * Amount of padding in pixels on all sides to add when updating the Google Maps camera for the
     * first time.
     */
    private static final int MAP_INITIAL_PADDING = 50;

    static final String KEY_SHELTERS = "shelters";

    private List<Shelter> shelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        shelters = getIntent().getParcelableArrayListExtra(KEY_SHELTERS);
        if (shelters == null) {
            throw new IllegalArgumentException("Expected to receive an ArrayList of shelters via " +
                "the " + KEY_SHELTERS + " extra.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        // Keep track of every marker we put down
        LatLngBounds.Builder boundaryBuilder = new LatLngBounds.Builder();

        for (Shelter shelter : shelters) {
            // Add a marker (pin) for every shelter that was given to us
            LatLng pos = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            map.addMarker(new MarkerOptions()
                .position(pos)
                .title(shelter.getName())
                .snippet(shelter.getSpecialNotes()));

            boundaryBuilder = boundaryBuilder.include(pos);
        }

        LatLngBounds bounds = boundaryBuilder.build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_INITIAL_PADDING));
    }
}
