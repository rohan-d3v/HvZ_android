package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ZombieReportModel {
    final int DATABASE_ID;
    LatLng location;
    int numZombies;
    Date timeSighted;

    public ZombieReportModel(int DATABASE_ID) {
        this.DATABASE_ID = DATABASE_ID;
    }

    public LatLng getLocation() { return location; }
    public int getNumZombies() { return numZombies; }
    public Date getTimeSighted() { return timeSighted; }

    public void setLocation(LatLng location) { this.location = location; }
    public void setNumZombies(int numZombies) { this.numZombies = numZombies; }
    public void setTimeSighted(Date timeSighted) { this.timeSighted = timeSighted; }

    @Override
    public String toString() {
        return "ZombieReportModel{" +
                "DATABASE_ID=" + DATABASE_ID +
                ", location=" + location +
                ", numZombies=" + numZombies +
                ", timeSighted=" + timeSighted +
                '}';
    }
}
