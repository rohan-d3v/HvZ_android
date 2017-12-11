package edu.acase.hvz.hvz_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.requests.BaseReportRequest;
import edu.acase.hvz.hvz_app.reports.EditZombieReportActivity;

public abstract class PlayerActivity<ReportRequest extends BaseReportRequest, thisActivity extends PlayerActivity> extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    protected GoogleMap gmap;
    protected LatLng currentLocation;
    protected Logger logger;
    protected BaseReportRequest reportRequest;
    private final Map<Marker, MapMarker> markerMap = new HashMap<>();

    abstract void populateMapWithReports();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

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
        final Context context = this;
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                logger.debug("clicked on a marker");
                final Dialog dialog = new Dialog(context, R.style.reportDialogTheme);
                dialog.setContentView(R.layout.report_dialog);

                final MapMarker mapMarker = markerMap.get(marker);
                final TextView reportContents = ((TextView) dialog.findViewById(R.id.reportText));
                reportContents.setText(mapMarker.getReport().snippet());

                final Button editReportButton = (Button) dialog.findViewById(R.id.editReportButton);
                editReportButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logger.debug("Clicked edit button on a marker");
                        Intent edit = new Intent(context, EditZombieReportActivity.class);
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
    public void onMapLongClick(LatLng latLng) {

    }

}
