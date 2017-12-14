package edu.acase.hvz.hvz_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.requests.BaseReportRequest;

/** Base class for all player views. Sets up the google map and the markers, along with all functionality. */

public abstract class BasePlayerActivity<ReportModel extends BaseReportModel> extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    protected final Logger logger;
    protected final BaseReportRequest reportRequest;
    protected final Map<Marker, MapMarker> markerMap = new HashMap<>();
    protected GoogleMap gmap;
    protected LatLng currentLocation;
    protected final LatLng cwruQuad = new LatLng(41.50325, -81.60755);
    protected final LatLngBounds campusBounds = new LatLngBounds(new LatLng(41.502535, -81.608143), new LatLng(41.510880, -81.602874));

    /** defines which intent to switch to when the player is caught */
    abstract Class<?> getCaughtButtonIntentClass();
    /** defines which intent to switch to when the player wants to create a report */
    abstract Class<?> getCreateReportIntentClass();
    /** defines which intent to switch to when the player wants to edit a report */
    abstract Class<?> getEditReportIntentClass();
    /** defines which view to use for this activity - should be an R.layout.activity_x */
    abstract int getContentView();

    /** populate the gmap, adds all the markers */
    void populateMapWithReports() {
        List<ReportModel> reportList = reportRequest.getAll();
        for (ReportModel report: reportList) {
            MapMarker mapMarker = new MapMarker(report);
            Marker marker = gmap.addMarker(mapMarker.getMarkerOptions());
            markerMap.put(marker, mapMarker);
        }
    }

    /** Constructor to set the logger and the report request type
     * @param logger logger. this param is so subclasses can define the log tag
     * @param reportRequest the report request to use in this player activity (handles server http calls). Either a ZombieReportRequest or HumanReportRequest
     */
    public BasePlayerActivity(Logger logger, BaseReportRequest reportRequest) {
        this.reportRequest = reportRequest;
        this.logger = logger;
    }

    /** This function sets up the googlemap: it sets the bounds, zoom, and populates it with report markers.
     * It also sets up the modals that appear when the user clicks on a marker.
     * @param googleMap the google map
     */
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
        gmap.setLatLngBoundsForCameraTarget(campusBounds);
        gmap.setMinZoomPreference(15);

        // Center the camera on campus
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cwruQuad));

        populateMapWithReports();

        // https://developers.google.com/maps/documentation/android-api/marker#info_windows
        gmap.setOnMarkerClickListener(marker -> {
            final Dialog dialog = new Dialog(context, R.style.reportDialogTheme);
            dialog.setContentView(R.layout.report_dialog);

            final MapMarker mapMarker = markerMap.get(marker);
            final TextView reportContents = ((TextView) dialog.findViewById(R.id.reportText));
            reportContents.setText(mapMarker.getReport().getReportContents());

            final Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
            editReportButton.setOnClickListener(view -> {
                Intent edit = new Intent(context, getEditReportIntentClass());
                edit.putExtra("mapMarker", mapMarker);
                //edit.putExtra("oldMarkerPosition", mapMarker.getMarkerOptions().getPosition());      <- for move methods
                if (edit.getExtras() != null)
                    logger.debug(true, "extras: ", edit.getExtras().toString());
                dialog.dismiss();
                startActivity(edit);
            });

            final Button deleteReportButton = (Button) dialog.findViewById(R.id.deleteReportButton);
            deleteReportButton.setOnClickListener(view -> {
                logger.debug("Clicked delete button on a marker");
                if (reportRequest.delete(mapMarker.getReport())) {
                    markerMap.remove(marker);
                    marker.remove();
                    dialog.dismiss();
                }
                else
                    logger.error(true, "Could not delete report", mapMarker.getReport().toString());
            });

            final Button moveReportButton = (Button) dialog.findViewById(R.id.moveReportButton);
            moveReportButton.setOnClickListener(view -> {
                Intent edit = new Intent(getBaseContext(), MoveMarker.class);
                edit.putExtra("mapMarker", mapMarker);
                startActivity(edit);
            });

            dialog.show();
            return true;
        });
    }

    /** This function sets up the view: it chooses which view to display, then adds all the button callbacks.
     * @param savedInstanceState the instance passed in when this view is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
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
                    .addOnSuccessListener(activity, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                        else {
                            logger.error("Could not get current location. Setting default...");
                            currentLocation = cwruQuad;
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

        helpButton.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://hvz.case.edu/contact/new");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        infoButton.setOnClickListener(view -> CommonDialogs.getInfoButtonDialog(context, view));

        caughtButton.setOnClickListener(view -> {
            Intent i = new Intent(context, getCaughtButtonIntentClass());
            startActivity(i);
            finish(); //prevent back button
        });

        reportButton.setOnClickListener(view -> {
            if (currentLocation != null) {
                Intent edit = new Intent(context, getCreateReportIntentClass());
                edit.putExtra("location", currentLocation);
                startActivity(edit);
            }
            else
                logger.error("ReportButton: Current location is null");
        });
    }

    /** Creates a new report when the user long clicks on the google map
     * @param clickLocation the location where the user clicked
     */
    @Override
    public void onMapLongClick(LatLng clickLocation) {
        Intent edit = new Intent(getBaseContext(), getCreateReportIntentClass());
        edit.putExtra("location", clickLocation);
        startActivity(edit);
    }
}
