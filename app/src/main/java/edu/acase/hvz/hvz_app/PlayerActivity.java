package edu.acase.hvz.hvz_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.requests.BaseReportRequest;

public abstract class PlayerActivity<ReportModel extends BaseReportModel> extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    protected final Logger logger;
    protected final BaseReportRequest reportRequest;
    protected final Map<Marker, MapMarker> markerMap = new HashMap<>();
    protected GoogleMap gmap;
    protected LatLng currentLocation;

    abstract Class<?> getCaughtButtonIntentClass();
    abstract Class<?> getCreateReportIntentClass();
    abstract Class<?> getEditReportIntentClass();

    void populateMapWithReports() {
        List<ReportModel> reportList = reportRequest.getAll();
        for (ReportModel report: reportList) {
            MapMarker mapMarker = new MapMarker(report);
            Marker marker = gmap.addMarker(mapMarker.getMarkerOptions());
            markerMap.put(marker, mapMarker);
        }
    }

    public PlayerActivity(Logger logger, BaseReportRequest reportRequest) {
        this.reportRequest = reportRequest;
        this.logger = logger;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        final Context context = this;

        // general config options
        gmap.setIndoorEnabled(false);
        gmap.setTrafficEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);;
        gmap.setOnMapLongClickListener(this);

        // Set the campus bounds
        LatLngBounds campusBounds = new LatLngBounds(new LatLng(41.502535, -81.608143), new LatLng(41.510880, -81.602874));
        gmap.setLatLngBoundsForCameraTarget(campusBounds);
        gmap.setMinZoomPreference(15);

        // Center the camera on campus
        LatLng cwruQuad = new LatLng(41.50325, -81.60755);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));

        populateMapWithReports();

        // https://developers.google.com/maps/documentation/android-api/marker#info_windows
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final Dialog dialog = new Dialog(context, R.style.reportDialogTheme);
                dialog.setContentView(R.layout.report_dialog);

                final MapMarker mapMarker = markerMap.get(marker);
                final TextView reportContents = ((TextView) dialog.findViewById(R.id.reportText));
                reportContents.setText(mapMarker.getReport().getReportContents());

                final Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
                editReportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent edit = new Intent(context, getEditReportIntentClass());
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
                    @Override
                    public void onClick(View v) {
                        logger.debug("Clicked delete button on a marker");
                        if (reportRequest.delete(mapMarker.getReport())) {
                            markerMap.remove(marker);
                            marker.remove();
                            dialog.dismiss();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human);
        final Context context = this;
        final Activity activity = this;

        // Getting current location
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                            else
                                logger.error("Could not get current location");
                        }
                    });
        }

        // actual map fragments and buttons
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button helpButton = (Button) findViewById(R.id.helpButton),
                infoButton = (Button) findViewById(R.id.infoButton),
                caughtButton = (Button) findViewById(R.id.caughtButton),
                reportButton = (Button) findViewById(R.id.reportButton);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogs.getHelpButtonDialog(context, v);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogs.getInfoButtonDialog(context, v);
            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, getCaughtButtonIntentClass());
                startActivity(i);
                finish(); //prevent back button
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currentLocation == null)
                    System.out.print("null");
                else{
                    Intent edit = new Intent(context, getCreateReportIntentClass());
                    edit.putExtra("location", currentLocation);
                    startActivity(edit);
                }
            }
        });
    }

    @Override
    public void onMapLongClick(LatLng clickLocation) {
        Intent edit = new Intent(getBaseContext(), getCreateReportIntentClass());
        edit.putExtra("location", clickLocation);
        startActivity(edit);
    }
}
