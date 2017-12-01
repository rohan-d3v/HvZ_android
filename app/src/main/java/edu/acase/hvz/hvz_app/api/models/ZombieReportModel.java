package edu.acase.hvz.hvz_app.api.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ZombieReportModel extends BaseReportModel {
    private int numZombies;

    public final static class SERIALIZATION extends BaseReportModel.SERIALIZATION {
        public final static String
                ARRAY_KEY = "zombie_reports",
                SINGLE_KEY = "zombie_report",
                NUM_ZOMBIES = "num_zombies";
    }

    public ZombieReportModel(int DATABASE_ID, int GAME_ID) {
        super(DATABASE_ID, GAME_ID);
    }
    public ZombieReportModel(int GAME_ID) {
        super(GAME_ID);
    }

    public int getNumZombies() { return numZombies; }

    public void setNumZombies(int numZombies) { this.numZombies = numZombies; }

    @Override
    public String snippet() {
        return "Num Zombies = " + numZombies + "\n" +
                "Time Sighted = " + timeSighted + "\n";
    }

    @Override
    public String toString() {
        return "ZombieReportModel{" +
                "database_id=" + database_id +
                "GAME_ID=" + GAME_ID +
                ", location=" + location +
                ", numZombies=" + numZombies +
                ", timeSighted=" + timeSighted +
                '}';
    }
}
