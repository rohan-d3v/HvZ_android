package edu.acase.hvz.hvz_app;

import com.google.android.gms.maps.model.MarkerOptions;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public class MapMarker {
    private MarkerOptions markerOptions;
    private BaseReportModel report;

    public MapMarker(BaseReportModel report) {
        this.report = report;
        markerOptions = new MarkerOptions().position(report.getLocation()).snippet(report.snippet());
    }

    public MarkerOptions getMarkerOptions() { return markerOptions; }
    public BaseReportModel getReport() { return report; }
}
