package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

/** Deserializer for HumanReports since they are returned as json from calls to the server
 * @see BaseReportDeserializer the abstract base deserializer */

public class HumanReportDeserializer extends BaseReportDeserializer<HumanReportModel> {
    public HumanReportDeserializer() {
        super("human_report_deserializer");
    }

    /** Deserialize the json report from a server call into the java object form
     * @param json the report as json
     * @param type the type
     * @param context the deserialization context to use
     * @return the java object form of this report, or null if something went wrong
     */
    @Override
    public HumanReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        try {
            JsonObject object = json.getAsJsonObject();
            if (object.getAsJsonObject(HumanReportModel.SERIALIZATION.SINGLE_KEY) != null) {
                object = object.getAsJsonObject(HumanReportModel.SERIALIZATION.SINGLE_KEY);
            }
            HumanReportModel report = new HumanReportModel(
                    object.get(HumanReportModel.SERIALIZATION.DATABASE_ID).getAsInt(),
                    object.get(HumanReportModel.SERIALIZATION.GAME_ID).getAsInt());
            report.setNumHumans(object.get(HumanReportModel.SERIALIZATION.NUM_HUMANS).getAsInt());
            report.setTypicalMagSize(object.get(HumanReportModel.SERIALIZATION.TYPICAL_MAG_SIZE).getAsInt());
            report.setTimeSighted(deserializeDate(object.get(HumanReportModel.SERIALIZATION.TIME_SIGHTED)));
            report.setLocation(new LatLng(
                    object.get(HumanReportModel.SERIALIZATION.LOCATION_LAT).getAsDouble(),
                    object.get(HumanReportModel.SERIALIZATION.LOCATION_LNG).getAsDouble()));
            return report;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
