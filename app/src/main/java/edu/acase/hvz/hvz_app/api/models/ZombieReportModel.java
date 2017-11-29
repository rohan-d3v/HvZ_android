package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ZombieReportModel extends BaseReportModel {
    private int numZombies;

    public final static class SERIALIZATION extends BaseReportModel.SERIALIZATION {
        public final static String
                ARRAY_KEY = "zombie_reports",
                NUM_ZOMBIES = "num_zombies";
    }

    public ZombieReportModel(int DATABASE_ID) {
        super(DATABASE_ID);
    }

    public LatLng getLocation() { return location; }
    public int getNumZombies() { return numZombies; }
    public Date getTimeSighted() { return timeSighted; }

    public void setLocation(LatLng location) { this.location = location; }
    public void setNumZombies(int numZombies) { this.numZombies = numZombies; }
    public void setTimeSighted(Date timeSighted) { this.timeSighted = timeSighted; }

    @Override
    public String snippet() {
        return "Num Zombies = " + numZombies + "\n" +
                "Time Sighted = " + timeSighted + "\n";
    }

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
