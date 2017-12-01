package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public abstract class BaseReportModel {
    final int DATABASE_ID;
    final int GAME_ID;
    LatLng location;
    Date timeSighted;

    public BaseReportModel(int DATABASE_ID, int GAME_ID) {
        this.DATABASE_ID = DATABASE_ID;
        this.GAME_ID = GAME_ID;
    }
    public BaseReportModel(int GAME_ID) {
        this.DATABASE_ID = -1;
        this.GAME_ID = GAME_ID;
    }

    public abstract String snippet();

    public static class SERIALIZATION {
        public final static String
                DATABASE_ID = "id",
                GAME_ID = "game_id",
                LOCATION_LAT = "location_lat",
                LOCATION_LNG = "location_long",
                TIME_SIGHTED = "time_sighted";
    }

    public int getDATABASE_ID() { return DATABASE_ID; }
    public int getGAME_ID() { return GAME_ID; }
    public LatLng getLocation() { return location; }
    public Date getTimeSighted() { return timeSighted; }

    public void setLocation(LatLng location) { this.location = location; }
    public void setTimeSighted(Date timeSighted) { this.timeSighted = timeSighted; }
}
