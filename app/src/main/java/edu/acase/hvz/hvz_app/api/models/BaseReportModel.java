package edu.acase.hvz.hvz_app.api.models;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public abstract class BaseReportModel implements Parcelable {
    final int GAME_ID;
    int database_id;
    LatLng location;
    Date timeSighted;

    public BaseReportModel(int database_id, int GAME_ID) {
        this.database_id = database_id;
        this.GAME_ID = GAME_ID;
    }
    public BaseReportModel(int GAME_ID) {
        this.database_id = -1;
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

    public int getDatabase_id() { return database_id; }
    public int getGAME_ID() { return GAME_ID; }
    public LatLng getLocation() { return location; }
    public Date getTimeSighted() { return timeSighted; }

    public void setLocation(LatLng location) { this.location = location; }
    public void setTimeSighted(Date timeSighted) { this.timeSighted = timeSighted; }
    public void setDatabase_id(int database_id) { this.database_id = database_id; }
}
