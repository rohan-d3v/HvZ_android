package edu.acase.hvz.hvz_app.api;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class HumanReportParcellingTest {
    @Test
    public void checkConsistency() {
        HumanReportModel report = getMockReportModel();
        for (int i=0; i < 10; i++) {
            Parcel parcel = Parcel.obtain();
            report.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            HumanReportModel reportFromParcel = HumanReportModel.CREATOR.createFromParcel(parcel);
            assertEquals(report, reportFromParcel);
        }
    }

    public static HumanReportModel getMockReportModel() {
        HumanReportModel report = new HumanReportModel(10, 3);
        report.setLocation(new LatLng(1.1, 0.9));
        report.setNumHumans(22);
        report.setTypicalMagSize(12);
        report.setTimeSighted(new Date());
        return report;
    }
}
