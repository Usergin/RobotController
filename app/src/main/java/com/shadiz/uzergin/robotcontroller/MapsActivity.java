package com.shadiz.uzergin.robotcontroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.shadiz.uzergin.robotcontroller.handlecontrol.HandleControlActivity;
import com.shadiz.uzergin.robotcontroller.model.OrientationValue;
import com.shadiz.uzergin.robotcontroller.view.CompassView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int REQUEST_LOCATION = 14;
    @Inject
    EventBus eventBus;
    @BindView(R.id.fabtoolbar_fab)
    View fab;
    @BindView(R.id.connectServer)
    View connectServer;
    @BindView(R.id.bluetooth)
    View bluetooth;
    @BindView(R.id.handleControl)
    View handleControl;
    private CompassView compassView;
    private FABToolbarLayout layout;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

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
        Toast.makeText(this, "ic_bluetooth", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HandleControlActivity.class));
    }

    //
    @OnClick(R.id.fabtoolbar_fab)
    public void fabtoolbarFab(View view) {
        layout.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        compassView = (CompassView) findViewById(R.id.compassView);

        if (checkLocationPermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            initialMap();
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

    private void initialMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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
        if (layout.showContextMenu())
            layout.hide();
        else
            super.onBackPressed();
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

//        checkLocationPermission();
        // Add a marker in Sydney and move the camera
        map.setMyLocationEnabled(true);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            map.setPadding(0, 500, 0, 0);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            map.setPadding(0, 700, 0, 0);
        }

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
                initialMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission_group.LOCATION},
                        REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Element clicked", Toast.LENGTH_SHORT).show();
    }

}
