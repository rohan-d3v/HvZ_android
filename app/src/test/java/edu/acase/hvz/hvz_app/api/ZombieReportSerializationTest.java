package edu.acase.hvz.hvz_app.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import edu.acase.hvz.hvz_app.api.deserializers.ZombieReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.serializers.ZombieReportSerializer;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ZombieReportSerializationTest {
    final ZombieReportSerializer serializer = new ZombieReportSerializer();
    final ZombieReportDeserializer deserializer = new ZombieReportDeserializer();

    @Test
    public void checkConsistency() {
        ZombieReportModel report = getMockReportModel();
        for (int i=0; i < 10; i++) {
            JsonElement json = serializer.serialize(report);
            ZombieReportModel result = deserializer.deserialize(json);
            assertEquals(report, result);
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
