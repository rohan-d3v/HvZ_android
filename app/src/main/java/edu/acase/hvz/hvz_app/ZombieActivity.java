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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

public class ZombieActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap gmap;
    private Map<Marker, MapMarker> markerMap = new HashMap<>();
    protected final String LOG_TAG = "human_report";
    protected final Logger logger = new Logger(LOG_TAG);

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
        HumanReportRequest humanReportRequest = new HumanReportRequest();
        List<HumanReportModel> zombieReports = humanReportRequest.getAll();
        for (HumanReportModel zombieReport: zombieReports) {
            MapMarker mapMarker = new MapMarker(zombieReport);
            Marker marker = gmap.addMarker(mapMarker.getMarkerOptions());
            markerMap.put(marker, mapMarker);

        }
        addHeatMap();
        //specify custom marker format

        // https://developers.google.com/maps/documentation/android-api/marker#info_windows
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                logger.debug("clicked on a marker");
                final Dialog dialog = new Dialog(ZombieActivity.this);
                dialog.setContentView(R.layout.custom_marker_info_contents);

                TextView snippet = ((TextView) dialog.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
                editReportButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logger.debug("clicked edit button on a marker");
                        Intent edit = new Intent(ZombieActivity.this, EditZ.class);
                        MapMarker mapMarker = markerMap.get(marker);
                        edit.putExtra("mapMarker", mapMarker);
                        //edit.putExtra("oldMarkerOptions", mapMarker.getMarkerOptions());
                        edit.putExtra("oldMarkerPosition", mapMarker.getMarkerOptions().getPosition());
                        logger.debug(true, "extras: ", edit.getExtras().toString());
                        dialog.hide();
                        startActivityForResult(edit, 1);

                        // TODO
                        /* around here you need code to handle the response after the editactivity returns
                         * in order to update the marker itself & this dialog...
                         * I have some jank code to update the marker at the bottom but pls do that sort of thing here instead.
                         * Cause the dialog is the display for that marker info
                         * So we need to update what it's showing */

                    }
                });

                dialog.show();
                return true;
            }
        });
    }
    private void addHeatMap() {
        ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
        List<ZombieReportModel> hReport = zombieReportRequest.getAll();
        ArrayList<LatLng> pos  = new ArrayList<LatLng>();
        for (int i = 0; i < hReport.size(); i++) {
            pos.add(hReport.get(i).getLoc());
        }

       HeatmapTileProvider provider = new HeatmapTileProvider.Builder().radius(50).data(pos).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        gmap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    MapMarker mapMarker = data.getParcelableExtra("mapMarker");
                    LatLng oldMarkerPosition = data.getParcelableExtra("oldMarkerPosition");
                    logger.debug("old pos: ", oldMarkerPosition.toString());
                    logger.debug(true, "edited mapMarker: ",mapMarker.toString());
                    //move marker, update


                    // TODO
                    /* this is real jank, pls don't use this in the final version
                     * maybe set up another map for locations -> markers
                     * to avoid this ridiculous o(N) lookup that shouldn't need to happen */

                    boolean updated = false;
                    for (Marker marker: markerMap.keySet()) {
                        LatLng markerPosition = markerMap.get(marker).getMarkerOptions().getPosition();
                        //logger.debug("pos: ",markerPosition.toString());
                        if (markerPosition.equals(oldMarkerPosition)) {
                            markerMap.remove(marker);
                            marker.remove();
                            Marker newMarker = gmap.addMarker(mapMarker.getMarkerOptions());
                            markerMap.put(newMarker, mapMarker);
                            updated = true;
                            break;
                        }
                    }
                    if (!updated)
                        logger.error(true, "could not find/update the map marker!", mapMarker.toString());
    }

    @Override
    public void onMapLongClick(LatLng location) {
        Intent edit = new Intent(this, EditZ.class);
        edit.putExtra("location", location);
        startActivityForResult(edit, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zombie);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //addHeatMap();
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
            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),StunnedActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        });

        final Button postDummy = (Button) findViewById(R.id.test_postZombieReport);
        postDummy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ZombieReportRequest request = new ZombieReportRequest();
                dummyReport = new ZombieReportModel(1);
                dummyReport.setLocation(new LatLng(666, -666));
                dummyReport.setTimeSighted(new Date());
                dummyReport.setNumZombies(666);
                dummyReport.setDatabase_id(request.create(dummyReport));
            }
        });

        final Button deleteDummy = (Button) findViewById(R.id.test_delteZombieReport);
        deleteDummy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ZombieReportRequest request = new ZombieReportRequest();
                if (dummyReport != null && dummyReport.getDatabase_id() >= 0) {
                    if (request.delete(dummyReport))
                        dummyReport = null;
                    else
                        logger.error("could not delete report");
                }
                else
                    logger.error("trying to delete nonexistent report");
            }
        });


    }

    private ZombieReportModel dummyReport;

    private AlertDialog.Builder getModalBuilder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        else
            return new AlertDialog.Builder(context);
    }
}
