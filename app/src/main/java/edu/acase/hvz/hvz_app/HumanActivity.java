package edu.acase.hvz.hvz_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

public class HumanActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap gmap;
    private Map<Marker, MapMarker> markerMap = new HashMap<>();
    protected final String LOG_TAG = "human_report";
    protected final Logger logger = new Logger(LOG_TAG);
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng loc;
    private final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setIndoorEnabled(false);
        gmap.setTrafficEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);;
        //gmap.setMyLocationEnabled(true);


        // Set the campus bounds
        LatLng cwruQuad = new LatLng(41.50325, -81.60755);
        LatLngBounds campusBounds = new LatLngBounds(new LatLng(41.502535, -81.608143), new LatLng(41.510880, -81.602874));
        gmap.setLatLngBoundsForCameraTarget(campusBounds);
        gmap.setMinZoomPreference(15);
        gmap.setOnMapLongClickListener(this);

        // Center the camera on campus
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));

        // populate with reports
        List<ZombieReportModel> zombieReports = zombieReportRequest.getAll();
        for (ZombieReportModel zombieReport: zombieReports) {
            MapMarker mapMarker = new MapMarker(zombieReport);
            Marker marker = gmap.addMarker(mapMarker.getMarkerOptions());
            markerMap.put(marker, mapMarker);
        }
        //specify custom marker format

        // https://developers.google.com/maps/documentation/android-api/marker#info_windows
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                logger.debug("clicked on a marker");
                final Dialog dialog = new Dialog(HumanActivity.this);
                dialog.setContentView(R.layout.custom_marker_info_contents);

                final MapMarker mapMarker = markerMap.get(marker);
                final TextView reportContents = ((TextView) dialog.findViewById(R.id.snippet));
                reportContents.setText(mapMarker.getReport().snippet());

                final Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
                editReportButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logger.debug("Clicked edit button on a marker");
                        Intent edit = new Intent(HumanActivity.this, EditZ.class);
                        edit.putExtra("mapMarker", mapMarker);
                        edit.putExtra("oldMarkerPosition", mapMarker.getMarkerOptions().getPosition());
                        if (edit.getExtras() != null)
                            logger.debug(true, "extras: ", edit.getExtras().toString());
                        dialog.dismiss();
                        startActivity(edit);
                    }
                });
                final Button deleteReportButton = (Button) dialog.findViewById(R.id.deleteReportButton);
                deleteReportButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logger.debug("Clicked delete button on a marker");
                        if (zombieReportRequest.delete((ZombieReportModel) mapMarker.getReport())) {
                            markerMap.remove(marker);
                            marker.remove();
                            dialog.hide();
                        }
                        else
                            logger.error(true, "Could not delete report", mapMarker.getReport().toString());
                    }
                });

                dialog.show();
                return true;
            }
        });
    }
    @Override
    public void onMapLongClick(LatLng location) {
        Intent edit = new Intent(getBaseContext(), createZ.class);
        edit.putExtra("location", location);
        startActivity(edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human);

        //Getting current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(HumanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(HumanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(HumanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                loc = new LatLng(location.getLatitude(),location.getLongitude());
                            }
                            else
                                System.out.print("null");
                        }
                    });
        }

        //actual map fragments and buttons
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button helpButton = (Button) findViewById(R.id.helpButton),
                     infoButton = (Button) findViewById(R.id.infoButton),
                     caughtButton = (Button) findViewById(R.id.caughtButton),
                    reportButton = (Button) findViewById(R.id.reportButton);
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
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (loc == null)
                System.out.print("null");
            else{
                Intent edit = new Intent(getBaseContext(), createZ.class);
                edit.putExtra("location", loc);
                startActivity(edit);
            }

            }
        });
    }
}
