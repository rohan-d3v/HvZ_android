package edu.acase.hvz.hvz_app;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.MarkerOptions;


import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public class MapMarker implements Parcelable {
    private MarkerOptions markerOptions;
    private BaseReportModel report;

    public MapMarker(BaseReportModel report) {
        setReport(report);
    }

    public MarkerOptions getMarkerOptions() { return markerOptions; }
    public <T extends BaseReportModel> T getReport() { return (T) report; }

    public void setReport(BaseReportModel report) {
        this.report = report;
        markerOptions = new MarkerOptions()
                .position(report.getLocation())
                .snippet(report.getReportContents());
    }

    //parcelling things
    public MapMarker(Parcel in) {
        markerOptions = (MarkerOptions) in.readValue(MarkerOptions.class.getClassLoader());
        report = (BaseReportModel) in.readValue(BaseReportModel.class.getClassLoader());
    }

    @Override
    public int describeContents() { return 0; }

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
