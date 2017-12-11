package edu.acase.hvz.hvz_app.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class HumanReportModel extends BaseReportModel {
    private int numHumans, typicalMagSize;

    public final static class SERIALIZATION extends BaseReportModel.SERIALIZATION {
        public final static String
                ARRAY_KEY = "human_reports",
                SINGLE_KEY = "human_report",
                NUM_HUMANS = "num_humans",
                TYPICAL_MAG_SIZE = "typical_mag_size";
    }

    public HumanReportModel(int DATABASE_ID, int GAME_ID) {
        super(DATABASE_ID, GAME_ID);
    }
    public HumanReportModel(int GAME_ID) {
        super(GAME_ID);
    }

    public int getNumHumans() { return numHumans; }
    public int getTypicalMagSize() { return typicalMagSize; }

    public void setNumHumans(int numHumans) { this.numHumans = numHumans; }
    public void setTypicalMagSize(int typicalMagSize) { this.typicalMagSize = typicalMagSize; }

    @Override
    public String snippet() {
        return "Num Humans = " + numHumans + "\n" +
                "Time Sighted = " + getTimeSinceSighted() + "\n" +
                "Typical Mag size = " + typicalMagSize + "\n";
    }

    @Override
    public String toString() {
        return "HumanReportModel{" +
                "database_id=" + database_id +
                ", GAME_ID=" + GAME_ID +
                ", location=" + location +
                ", numHumans=" + numHumans +
                ", typicalMagSize=" + typicalMagSize +
                ", timeSighted=" + timeSighted +
                '}';
    }


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
        dest.writeInt(numHumans);
        dest.writeInt(typicalMagSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HumanReportModel> CREATOR = new Parcelable.Creator<HumanReportModel>() {
        @Override
        public HumanReportModel createFromParcel(Parcel in) {
            HumanReportModel report = new HumanReportModel(in.readInt());
            report.database_id = in.readInt();
            report.location = in.readParcelable(LatLng.class.getClassLoader());
            long tmpTimeSighted = in.readLong();
            report.timeSighted = tmpTimeSighted != -1 ? new Date(tmpTimeSighted) : null;
            report.numHumans = in.readInt();
            report.typicalMagSize = in.readInt();
            return report;
        }

        @Override
        public HumanReportModel[] newArray(int size) {
            return new HumanReportModel[size];
        }
    };
}
