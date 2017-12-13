package edu.acase.hvz.hvz_app.api.models;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/** The base report model (all reports derive from this class) */

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

    /** Get the contents of this report
     * @return the contents to display in marker popup dialogs
     */
    public abstract String getReportContents();

    /** The serialization details for this report. These are the "magic strings"
     * in the json returned from the server, and are used to serialize and deserialize
     * reports when communicating with the server. */
    public static class SERIALIZATION {
        public static final String
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

    /** Get a string representing how long it's been since the sighting was reported
     * @return the string representation
     */
    protected String getTimeSinceSighted() {
        long msDelta = new Date().getTime() - timeSighted.getTime();
        long minsSince = TimeUnit.MINUTES.convert(msDelta, TimeUnit.MILLISECONDS);
        return minsSince + " mins ago";
    }


    // base overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseReportModel that = (BaseReportModel) o;

        if (GAME_ID != that.GAME_ID) return false;
        if (database_id != that.database_id) return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;
        return timeSighted != null ? timeSighted.equals(that.timeSighted) : that.timeSighted == null;
    }

    @Override
    public int hashCode() {
        int result = GAME_ID;
        result = 31 * result + database_id;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (timeSighted != null ? timeSighted.hashCode() : 0);
        return result;
    }
}
