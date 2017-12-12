package edu.acase.hvz.hvz_app.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import org.junit.Test;

import edu.acase.hvz.hvz_app.api.deserializers.HumanReportDeserializer;
import edu.acase.hvz.hvz_app.api.deserializers.ZombieReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.serializers.HumanReportSerializer;
import edu.acase.hvz.hvz_app.api.serializers.ZombieReportSerializer;

import static org.junit.Assert.assertEquals;

public class HumanReportSerializationTest extends BaseReportTest {
    final HumanReportSerializer serializer = new HumanReportSerializer();
    final HumanReportDeserializer deserializer = new HumanReportDeserializer();

    public static class Mock {
        static final int database_id = 10, game_id = 3, numHumans = 22, typicalMagSize = 12;
        static final LatLng location = new LatLng(1.1, 0.9);
        static final String dateString = "08/27/2016";

        public static HumanReportModel getReportModel() {
            HumanReportModel report = new HumanReportModel(database_id, game_id);
            report.setLocation(location);
            report.setNumHumans(numHumans);
            report.setTypicalMagSize(12);
            report.setTimeSighted(stringToDate(dateString));
            return report;
        }
    }

    @Test
    public void checkConsistency() {
        HumanReportModel report = Mock.getReportModel();
        for (int i=0; i < 10; i++) {
            JsonElement json = serializer.serialize(report);
            HumanReportModel result = deserializer.deserialize(json);
            assertEquals(report, result);
        }
    }
}
