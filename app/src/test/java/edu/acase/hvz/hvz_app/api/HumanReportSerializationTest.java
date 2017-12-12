package edu.acase.hvz.hvz_app.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import org.junit.Test;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.deserializers.HumanReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.serializers.HumanReportSerializer;

import static org.junit.Assert.assertEquals;

public class HumanReportSerializationTest {
    final HumanReportSerializer serializer = new HumanReportSerializer();
    final HumanReportDeserializer deserializer = new HumanReportDeserializer();

    @Test
    public void checkConsistency() {
        HumanReportModel report = getMockReportModel();
        for (int i=0; i < 10; i++) {
            JsonElement json = serializer.serialize(report);
            HumanReportModel result = deserializer.deserialize(json);
            assertEquals(report, result);
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
