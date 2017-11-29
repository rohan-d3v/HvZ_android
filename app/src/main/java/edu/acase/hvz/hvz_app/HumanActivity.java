package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

public class HumanActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap gmap;
    private static final int EDIT_REQUEST = 1;

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

        // populate with reports
        ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
        List<ZombieReportModel> zombieReports = zombieReportRequest.fetchAll();
        for (ZombieReportModel zombieReport: zombieReports) {
            gmap.addMarker(new MarkerOptions().position(zombieReport.getLocation()).snippet(zombieReport.snippet()));
        }
        gmap.addMarker(new MarkerOptions().position(cwruQuad).snippet("This is where the report goes..."));

        gmap.setInfoWindowAdapter(new mapInfoWindowAdapter());
    }

    @Override
    public void onMapLongClick(LatLng point) {
        /*Intent edit = new Intent(HumanActivity.this, HumanReport.class);
        edit.putExtra("location", point);
        HumanActivity.this.startActivityForResult(edit, EDIT_REQUEST); */
        Marker newMarker = gmap.addMarker(new MarkerOptions()
                .position(point)
                .snippet(point.toString()));
        newMarker.setTitle(newMarker.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button helpButton = (Button) findViewById(R.id.helpButton),
                     infoButton = (Button) findViewById(R.id.infoButton),
                     caughtButton = (Button) findViewById(R.id.caughtButton);

        final Context context = this;
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonDialogs.getHelpButtonDialog(context, v);
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonDialogs.getInfoButtonDialog(context, v);
            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),IncubatingActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        });
        final Button getZombieReportsButton = (Button) findViewById(R.id.test_getZombieReports);
        getZombieReportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ZombieReportRequest request = new ZombieReportRequest();
                request.fetchAll();
            }
        });
    }

    private AlertDialog.Builder getModalBuilder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        else
            return new AlertDialog.Builder(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (EDIT_REQUEST) : {
                if (resultCode == Activity.RESULT_OK) {
                    MarkerOptions markerOptions = data.getParcelableExtra("marker");
                    gmap.addMarker(markerOptions);
                }
                break;
            }
        }
    }
}
