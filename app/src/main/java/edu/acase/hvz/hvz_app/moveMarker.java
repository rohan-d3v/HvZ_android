package edu.acase.hvz.hvz_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

import static edu.acase.hvz.hvz_app.HumanActivity.logger;

public class moveMarker extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final Logger logger = new Logger("edit_marker_location");

    protected GoogleMap gmap;
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
    private MarkerOptions marker;
    private MapMarker mapMarker;

    @Override
    public void onMapClick(LatLng clickLocation) {
        marker.position(clickLocation);
        updateLoc(marker);
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_marker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapMarker = getIntent().getParcelableExtra("mapMarker");

    }

    private void updateLoc(MarkerOptions marker){
        logger.error(mapMarker.toString());

        //if (mapMarker.getReport() instanceof ZombieReportModel){
        ZombieReportModel report = mapMarker.getReport();
        report.setLocation(marker.getPosition());
        //logger.error("newLoc");
        report.setTimeSighted(new Date());
        zombieReportRequest.update(report);
        //}
 /*       else{
            HumanReportModel report = mapMarker.getReport();
            report.setLocation(newLoc);
            report.setTimeSighted(new Date());
            humanReportRequest.update(report);
        }
*/

    }

}
