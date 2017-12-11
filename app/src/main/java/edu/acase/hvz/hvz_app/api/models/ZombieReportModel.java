package edu.acase.hvz.hvz_app.api.models;

import android.os.Parcel;
import android.os.Parcelable;

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
    public LatLng getLoc(){return location;}
    @Override
    public String getReportContents() {
        return "Num Zombies = " + numZombies + "\n" +
                "Time Sighted = " + getTimeSinceSighted() + "\n";
    }

    @Override
    public String toString() {
        return "ZombieReportModel{" +
                "database_id=" + database_id +
                ", GAME_ID=" + GAME_ID +
                ", location=" + location +
                ", numZombies=" + numZombies +
                ", timeSighted=" + timeSighted +
                '}';
    }


    //parceling functions

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(GAME_ID);
        dest.writeInt(database_id);
        dest.writeParcelable(location, flags);
        dest.writeLong(timeSighted != null ? timeSighted.getTime() : -1L);
        dest.writeInt(numZombies);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ZombieReportModel> CREATOR = new Parcelable.Creator<ZombieReportModel>() {
        @Override
        public ZombieReportModel createFromParcel(Parcel in) {
            ZombieReportModel report = new ZombieReportModel(in.readInt());
            report.database_id = in.readInt();
            report.location = in.readParcelable(LatLng.class.getClassLoader());
            long tmpTimeSighted = in.readLong();
            report.timeSighted = tmpTimeSighted != -1 ? new Date(tmpTimeSighted) : null;
            report.numZombies = in.readInt();
            return report;
        }

        @Override
        public ZombieReportModel[] newArray(int size) {
            return new ZombieReportModel[size];
        }
    };
}
