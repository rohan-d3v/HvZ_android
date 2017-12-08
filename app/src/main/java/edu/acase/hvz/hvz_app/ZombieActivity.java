package edu.acase.hvz.hvz_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

public class ZombieActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap gmap;
    private Map<Marker, MapMarker> markerMap = new HashMap<>();
    protected final String LOG_TAG = "zombie_report";
    protected final Logger logger = new Logger(LOG_TAG);
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();

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
        addHeatMap();
        // populate with reports
        List<HumanReportModel> humanReports = humanReportRequest.getAll();
        for (HumanReportModel humanReport: humanReports) {
            MapMarker mapMarker = new MapMarker(humanReport);
            Marker marker = gmap.addMarker(mapMarker.getMarkerOptions());
            markerMap.put(marker, mapMarker);
        }
        //specify custom marker format

        // https://developers.google.com/maps/documentation/android-api/marker#info_windows
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                logger.debug("clicked on a marker");
                final Dialog dialog = new Dialog(ZombieActivity.this);
                dialog.setContentView(R.layout.custom_marker_info_contents);

                final MapMarker mapMarker = markerMap.get(marker);
                final TextView reportContents = ((TextView) dialog.findViewById(R.id.snippet));
                reportContents.setText(mapMarker.getReport().snippet());

                final Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
                editReportButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logger.debug("clicked edit button on a marker");
                        Intent edit = new Intent(ZombieActivity.this, EditH.class);
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
                        if (humanReportRequest.delete((HumanReportModel) mapMarker.getReport())) {
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
        Intent edit = new Intent(getBaseContext(), createH.class);
        edit.putExtra("location", location);
        startActivity(edit);
    }
    private void addHeatMap() {
        ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
        List<ZombieReportModel> hReport = zombieReportRequest.getAll();
        ArrayList<LatLng> pos  = new ArrayList<LatLng>();
        for (int i = 0; i < hReport.size(); i++) {
            pos.add(hReport.get(i).getLoc());
        }
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                .05f, .2f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().gradient(gradient).radius(50).data(pos).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        gmap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
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
                CommonDialogs.getHelpButtonDialog(context, v);
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
    }
}
