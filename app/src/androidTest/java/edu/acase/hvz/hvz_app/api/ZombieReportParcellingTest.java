package edu.acase.hvz.hvz_app.api;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ZombieReportParcellingTest {
    @Test
    public void checkConsistency() {
        ZombieReportModel report = getMockReportModel();
        for (int i=0; i < 10; i++) {
            Parcel parcel = Parcel.obtain();
            report.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            ZombieReportModel reportFromParcel = ZombieReportModel.CREATOR.createFromParcel(parcel);
            assertEquals(report, reportFromParcel);
        }
    }

    public static ZombieReportModel getMockReportModel() {
        ZombieReportModel report = new ZombieReportModel(11, 5);
        report.setLocation(new LatLng(0.838, 0.772));
        report.setNumZombies(16);
        report.setTimeSighted(new Date());
        return report;
    }
}
