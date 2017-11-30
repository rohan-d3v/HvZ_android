package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.List;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

public class ZombieActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{
    private GoogleMap gmap;
    class mapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View view;

        public mapInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_marker_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            TextView snippet = ((TextView) view.findViewById(R.id.snippet));
            snippet.setText(marker.getSnippet());
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setIndoorEnabled(false);
        gmap.setTrafficEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);
        //gmap.setMyLocationEnabled(true); //needs a permissions check

        // Set the campus bounds
        LatLng cwruQuad = new LatLng(41.50325, -81.60755);
        LatLngBounds campusBounds = new LatLngBounds(new LatLng(41.502535, -81.608143), new LatLng(41.510880, -81.602874));
        gmap.setLatLngBoundsForCameraTarget(campusBounds);
        gmap.setMinZoomPreference(15);
        gmap.setOnMapLongClickListener(this);

        // Center the camera on campus
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));

        HumanReportRequest humanReportRequest = new HumanReportRequest();
        List<HumanReportModel> humanReports = humanReportRequest.fetchAll();
        for (HumanReportModel humanReport: humanReports) {
            gmap.addMarker(new MarkerOptions().position(humanReport.getLocation()).snippet(humanReport.snippet()));
        }

        gmap.addMarker(new MarkerOptions().position(cwruQuad).snippet("Quad"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));
        gmap.setMinZoomPreference(15);
        gmap.setOnMapLongClickListener(this);
        gmap.setInfoWindowAdapter(new ZombieActivity.mapInfoWindowAdapter());

    }

    @Override
    public void onMapLongClick(LatLng point) {
        Intent edit = new Intent(this, EditActivity.class);
        edit.putExtra("location", point);
        startActivityForResult(edit, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    MarkerOptions markerOptions = data.getParcelableExtra("marker");
                    gmap.addMarker(markerOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zombie);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button helpButton = (Button) findViewById(R.id.helpButton),
                infoButton = (Button) findViewById(R.id.infoButton),
                caughtButton = (Button) findViewById(R.id.caughtButton);

        final Context context = this;
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.stackoverflow.com/"));
                startActivity(viewIntent);

            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),HumanActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),StunnedActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        });

        final Button getZombieReportsButton = (Button) findViewById(R.id.test_getHuman);
        getZombieReportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ZombieReportRequest request = new ZombieReportRequest();
                request.fetchAll();
            }
        });
    }
}
