package edu.acase.hvz.hvz_app;

<<<<<<< HEAD
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
=======
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
>>>>>>> origin/master

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public class MapMarker implements Parcelable {
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

    public void setReport(BaseReportModel report) {
        this.report = report;
        markerOptions = new MarkerOptions().position(report.getLocation()).snippet(report.snippet());
    }

    //parcelling things
    public MapMarker(Parcel in) {
        markerOptions = (MarkerOptions) in.readValue(MarkerOptions.class.getClassLoader());
        report = (BaseReportModel) in.readValue(BaseReportModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        //TODO ?
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(markerOptions);
        dest.writeValue(report);
    }

    public static final Parcelable.Creator<MapMarker> CREATOR = new Parcelable.Creator<MapMarker>() {
        @Override
        public MapMarker createFromParcel(Parcel in) {
            return new MapMarker(in);
        }

        @Override
        public MapMarker[] newArray(int size) {
            return new MapMarker[size];
        }
    };

    @Override
    public String toString() {
        return "MapMarker{" +
                "markerOptions=" + markerOptions +
                ", report=" + report +
                '}';
    }
}
