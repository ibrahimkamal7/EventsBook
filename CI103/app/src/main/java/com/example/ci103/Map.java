package com.example.ci103;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements PermissionsListener, LocationEngine {
    private List<Double> latitude = new ArrayList<Double>();
    private List<Double> longitude = new ArrayList<Double>();
    private List<String> name = new ArrayList<String>();
    private Double myLat, myLong;
    private LocationManager locationManager;
    private MapView mapView;
    private LocationEngine locationEngine;
    private Location myLocation;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbarText);
        textView.setText("Event Map");


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        DatabaseReference event = FirebaseDatabase.getInstance().getReference("Events");
                        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(getApplicationContext());
                        event.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                                    Event event = keynode.getValue(Event.class);
                                    latitude.add(event.getLatitude());
                                    longitude.add(event.getLongitude());
                                    name.add(event.getEvent_name());

                                }
                                IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
                                Icon icon = iconFactory.fromResource(R.drawable.custom_marker);
                                for (int i = 0; i < latitude.size(); i++) {
                                    Double lat = latitude.get(i);
                                    Double lon = longitude.get(i);
                                    mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name.get(i)).setIcon(icon));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> callback) throws SecurityException {

    }

    @Override
    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull LocationEngineCallback<LocationEngineResult> callback, @Nullable Looper looper) throws SecurityException {

    }

    @Override
    public void requestLocationUpdates(@NonNull LocationEngineRequest request, PendingIntent pendingIntent) throws SecurityException {

    }

    @Override
    public void removeLocationUpdates(@NonNull LocationEngineCallback<LocationEngineResult> callback) {

    }

    @Override
    public void removeLocationUpdates(PendingIntent pendingIntent) {

    }
}
