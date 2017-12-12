package edu.acase.hvz.hvz_app.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import edu.acase.hvz.hvz_app.api.deserializers.ZombieReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.serializers.ZombieReportSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ZombieReportSerializationTest extends BaseReportTest {
    final ZombieReportSerializer serializer = new ZombieReportSerializer();
    final ZombieReportDeserializer deserializer = new ZombieReportDeserializer();

    public static class Mock {
        static final int database_id = 10, game_id = 3, numZombies = 22;
        static final LatLng location = new LatLng(1.1, 0.9);
        static final String dateString = "08/27/2016";

        public static ZombieReportModel getReportModel() {
            ZombieReportModel report = new ZombieReportModel(database_id, game_id);
            report.setLocation(location);
            report.setNumZombies(numZombies);
            report.setTimeSighted(stringToDate(dateString));
            return report;
        }
    }

    @Test
    public void checkConsistency() {
        ZombieReportModel report = Mock.getReportModel();
        for (int i=0; i < 10; i++) {
            JsonElement json = serializer.serialize(report);
            ZombieReportModel result = deserializer.deserialize(json);
            assertEquals(report, result);
        }
    }
}
