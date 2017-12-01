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

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

public class ZombieReportReportDeserializer extends BaseReportDeserializer<ZombieReportModel> {

    public List<ZombieReportModel> deserializeAll(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        List<ZombieReportModel> zombieReportModels = new ArrayList<>();
        JsonObject root = json.getAsJsonObject();

        try {
            if (root.has(ZombieReportModel.SERIALIZATION.ARRAY_KEY)) {
                JsonArray jsonArray = root.getAsJsonArray(ZombieReportModel.SERIALIZATION.ARRAY_KEY);
                for (JsonElement jsonElement : jsonArray) {
                    ZombieReportModel report = deserialize(jsonElement, null, null);
                    if (report != null)
                        zombieReportModels.add(report);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zombieReportModels;
    }

    @Override
    public ZombieReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        ZombieReportModel report = null;
        JsonObject object = json.getAsJsonObject();

        try {
            report = new ZombieReportModel(
                    object.get(ZombieReportModel.SERIALIZATION.DATABASE_ID).getAsInt(),
                    object.get(ZombieReportModel.SERIALIZATION.GAME_ID).getAsInt());
            report.setNumZombies(object.get(ZombieReportModel.SERIALIZATION.NUM_ZOMBIES).getAsInt());
            report.setTimeSighted(deserializeDate(object.get(ZombieReportModel.SERIALIZATION.TIME_SIGHTED)));
            report.setLocation(new LatLng(
                    object.get(ZombieReportModel.SERIALIZATION.LOCATION_LAT).getAsDouble(),
                    object.get(ZombieReportModel.SERIALIZATION.LOCATION_LNG).getAsDouble()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }
}
