package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ZombieReportModel {
    final int DATABASE_ID;
    LatLng location;
    int numHumans, typicalMagSize;
    Date timeSighted;

    public ZombieReportModel(int DATABASE_ID) {
        this.DATABASE_ID = DATABASE_ID;
    }

    public LatLng getLocation() { return location; }
    public int getNumHumans() { return numHumans; }
    public Date getTimeSighted() { return timeSighted; }
    public int getTypicalMagSize() { return typicalMagSize; }

    public void setLocation(LatLng location) { this.location = location; }
    public void setNumHumans(int numHumans) { this.numHumans = numHumans; }
    public void setTypicalMagSize(int typicalMagSize) { this.typicalMagSize = typicalMagSize; }
    public void setTimeSighted(Date timeSighted) { this.timeSighted = timeSighted; }
}
