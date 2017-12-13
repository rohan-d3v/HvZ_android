package edu.acase.hvz.hvz_app.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

/** The model representing human reports. This is essentially a java clone of the human report
 * object stored in the server
 * @see BaseReportModel the abstract base model */

public class HumanReportModel extends BaseReportModel {
    private int numHumans, typicalMagSize;

    /** The serialization details for this report. These are the "magic strings"
     * in the json returned from the server, and are used to serialize and deserialize
     * reports when communicating with the server.
     * @see BaseReportModel.SERIALIZATION */
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

    /** Get the contents of this report
     * @return the contents to display in marker popup dialogs
     */
    @Override
    public String getReportContents() {
        return "Num Humans = " + numHumans + "\n" +
                "Time Sighted = " + getTimeSinceSighted() + "\n" +
                "Typical Mag size = " + typicalMagSize + "\n";
    }


    //base overrides

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HumanReportModel that = (HumanReportModel) o;

        if (numHumans != that.numHumans) return false;
        return typicalMagSize == that.typicalMagSize;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + numHumans;
        result = 31 * result + typicalMagSize;
        return result;
    }

    //parcelling methods

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
