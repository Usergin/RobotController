package com.shadiz.uzergin.robotcontroller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shadiz.uzergin.robotcontroller.model.OrientationValue;
import com.shadiz.uzergin.robotcontroller.view.CompassView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 14;
    @Inject
    EventBus eventBus;
    @BindView(R.id.fabtoolbar)
    FABToolbarLayout layout;
    @BindView(R.id.connectServer)
    View connectServer;
    @BindView(R.id.bluetooth)
    View bluetooth;
    @BindView(R.id.handleControl)
    View handleControl;
    @BindView(R.id.fabtoolbar_fab)
    View fab;
    @BindView(R.id.compassView)
    CompassView compassView;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

//        connectServer.setOnClickListener(this);
//        bluetooth.setOnClickListener(this);
//        handleControl.setOnClickListener(this);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layout.show();
//            }
//        });
        if (checkLocationPermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrientationValue event) {
        Log.d("MapsActivity", "as" + event);
        if (compassView != null) {
            compassView.setBearing(event.getValue()[0]);
            compassView.setPitch(event.getValue()[1]);
            compassView.setRoll(-event.getValue()[2]);
            compassView.invalidate();
        }
    }

    ;

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        layout.hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.connectServer)
    public void connectServer(View view) {
        Toast.makeText(this, "ic_connect_to_server", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bluetooth)
    public void bluetooth(View view) {
        Toast.makeText(this, "ic_bluetooth", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.handleControl)
    public void handleControl(View view) {
        Toast.makeText(this, "ic_handle", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fabtoolbar_fab)
    public void fabtoolbarFab(View view) {
        Toast.makeText(this, " layout.show();", Toast.LENGTH_SHORT).show();
        layout.show();
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
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        checkLocationPermission();
        // Add a marker in Sydney and move the camera
        map.setMyLocationEnabled(true);
        map.setPadding(0, 700, 0, 0);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(true);
        LatLng spb = new LatLng(59.960879, 30.274053);
        map.addMarker(new MarkerOptions().position(spb).title("Текущее местоположение"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 17));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission_group.LOCATION},
                        REQUEST_LOCATION);
            }
        }
    }
}
