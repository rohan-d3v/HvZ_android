package edu.acase.hvz.hvz_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

import static edu.acase.hvz.hvz_app.HumanActivity.logger;

/** This is the activity that players are shown whenever they want to move a marker
 * that is already on the map. It shows a blank map for the player to choose a new
 * location for the marker to be placed. */

public class MoveMarker extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final Logger logger = new Logger("edit_marker_location");

    protected GoogleMap gmap;
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
    private MapMarker marker;
    private Marker newLoc;
        @Override
        public void onMapClick(LatLng clickLocation) {
            Intent i = new Intent (getApplicationContext(), HumanActivity.class);
            startActivity(i);
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        final Context context = this;

        // general config options
        gmap.setIndoorEnabled(false);
        gmap.setTrafficEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);

        // Set the campus bounds
        LatLngBounds campusBounds = new LatLngBounds(new LatLng(41.502535, -81.608143), new LatLng(41.510880, -81.602874));
        gmap.setLatLngBoundsForCameraTarget(campusBounds);
        gmap.setMinZoomPreference(15);

        // Center the camera on campus
        LatLng cwruQuad = new LatLng(41.50325, -81.60755);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));

        gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                newLoc = googleMap.addMarker(new MarkerOptions()
                        .position(latLng));
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_marker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        marker = getIntent().getParcelableExtra("mapMarker");
        Button saveButton = (Button) findViewById(R.id.button2);
        saveButton.setOnClickListener(view -> {
            updateLoc(newLoc);
        });
    }

    private void updateLoc(Marker newLoc){
        logger.error(marker.toString());

        if (marker.getReport() instanceof ZombieReportModel){
        ZombieReportModel report = marker.getReport();
        report.setLocation(newLoc.getPosition());
        //logger.error("newLoc");
        report.setTimeSighted(new Date());
        zombieReportRequest.update(report);
        Intent edit = new Intent(getBaseContext(), HumanActivity.class);
        startActivity(edit);
        }
      else if (marker.getReport() instanceof HumanReportModel){
            HumanReportModel report = marker.getReport();
            report.setLocation(newLoc.getPosition());
            report.setTimeSighted(new Date());
            humanReportRequest.update(report);
            Intent edit = new Intent(getBaseContext(), ZombieActivity.class);
            startActivity(edit);
        }

    }

}
