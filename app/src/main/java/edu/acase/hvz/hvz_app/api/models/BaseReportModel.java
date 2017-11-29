package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class BaseReportModel {
    final int DATABASE_ID;
    LatLng location;
    Date timeSighted;
    protected static List<BaseReportModel> allReports = new ArrayList<>();

    protected BaseReportModel(int DATABASE_ID) {
        this.DATABASE_ID = DATABASE_ID;
        allReports.add(this);
    }

    abstract String snippet();

    public static class SERIALIZATION {
        public final static String
                DATABASE_ID = "id",
                LOCATION_LAT = "location_lat",
                LOCATION_LNG = "location_long",
                TIME_SIGHTED = "time_sighted";
    }
}
