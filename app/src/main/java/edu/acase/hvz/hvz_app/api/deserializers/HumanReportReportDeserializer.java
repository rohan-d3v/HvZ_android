package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

public class HumanReportReportDeserializer extends BaseReportDeserializer<HumanReportModel> {

    public List<HumanReportModel> deserializeAll(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        List<HumanReportModel> humanReportModels = new ArrayList<>();
        JsonObject root = json.getAsJsonObject();

        try {
            if (root.has(HumanReportModel.SERIALIZATION.ARRAY_KEY)) {
                JsonArray jsonArray = root.getAsJsonArray(HumanReportModel.SERIALIZATION.ARRAY_KEY);
                for (JsonElement jsonElement : jsonArray) {
                    HumanReportModel report = deserialize(jsonElement, null, null);
                    if (report != null)
                        humanReportModels.add(report);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return humanReportModels;
    }

    @Override
    public HumanReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        HumanReportModel report = null;
        JsonObject object = json.getAsJsonObject();

        try {
            report = new HumanReportModel(
                    object.get(HumanReportModel.SERIALIZATION.DATABASE_ID).getAsInt(),
                    object.get(HumanReportModel.SERIALIZATION.GAME_ID).getAsInt());
            report.setNumHumans(object.get(HumanReportModel.SERIALIZATION.NUM_HUMANS).getAsInt());
            report.setTypicalMagSize(object.get(HumanReportModel.SERIALIZATION.TYPICAL_MAG_SIZE).getAsInt());
            report.setTimeSighted(deserializeDate(object.get(HumanReportModel.SERIALIZATION.TIME_SIGHTED)));
            report.setLocation(new LatLng(
                    object.get(HumanReportModel.SERIALIZATION.LOCATION_LAT).getAsDouble(),
                    object.get(HumanReportModel.SERIALIZATION.LOCATION_LNG).getAsDouble()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }
}
