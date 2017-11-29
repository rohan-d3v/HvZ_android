package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

abstract class BaseReportModel {
    final int DATABASE_ID;
    LatLng location;
    Date timeSighted;

    protected BaseReportModel(int DATABASE_ID) {
        this.DATABASE_ID = DATABASE_ID;
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
