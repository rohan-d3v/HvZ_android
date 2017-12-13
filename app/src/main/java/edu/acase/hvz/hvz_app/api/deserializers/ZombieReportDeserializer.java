package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

/** Deserializer for ZombieReports since they are returned as json from calls to the server
 * @see BaseReportDeserializer the abstract base deserializer */

public class ZombieReportDeserializer extends BaseReportDeserializer<ZombieReportModel> {
    public ZombieReportDeserializer() {
        super("zombie_report_deserializer");
    }

    /** Deserialize the json report from a server call into the java object form
     * @param json the report as json
     * @param type the type
     * @param context the deserialization context to use
     * @return the java object form of this report, or null if something went wrong
     */
    @Override
    public ZombieReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        try {
            JsonObject object = json.getAsJsonObject();
            if (object.getAsJsonObject(ZombieReportModel.SERIALIZATION.SINGLE_KEY) != null) {
                object = object.getAsJsonObject(ZombieReportModel.SERIALIZATION.SINGLE_KEY);
            }
            ZombieReportModel report = new ZombieReportModel(
                    object.get(ZombieReportModel.SERIALIZATION.DATABASE_ID).getAsInt(),
                    object.get(ZombieReportModel.SERIALIZATION.GAME_ID).getAsInt());
            report.setNumZombies(object.get(ZombieReportModel.SERIALIZATION.NUM_ZOMBIES).getAsInt());
            report.setTimeSighted(deserializeDate(object.get(ZombieReportModel.SERIALIZATION.TIME_SIGHTED)));
            report.setLocation(new LatLng(
                    object.get(ZombieReportModel.SERIALIZATION.LOCATION_LAT).getAsDouble(),
                    object.get(ZombieReportModel.SERIALIZATION.LOCATION_LNG).getAsDouble()));
            return report;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
