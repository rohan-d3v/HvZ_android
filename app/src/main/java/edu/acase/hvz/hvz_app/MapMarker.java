package edu.acase.hvz.hvz_app;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public class MapMarker {
    private MarkerOptions markerOptions;
    private BaseReportModel report;
    Date timeS;
    int numZ;
    public MapMarker(BaseReportModel report) {
        this.report = report;
        markerOptions = new MarkerOptions().position(report.getLocation()).snippet(report.snippet());
    }

    public MarkerOptions getMarkerOptions() { return markerOptions; }
    public MarkerOptions setMarkerOptions(LatLng lng, Date timeS, int numZ){
        markerOptions.position(lng);
        this.timeS = timeS;
        this.numZ = numZ;
        return markerOptions; }
    public BaseReportModel getReport() { return report; }
}
