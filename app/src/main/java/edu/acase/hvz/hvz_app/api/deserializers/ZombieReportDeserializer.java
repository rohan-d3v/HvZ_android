package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

public class ZombieReportDeserializer extends BaseReportDeserializer<ZombieReportModel> {
    public ZombieReportDeserializer() {
        super("zombie_report_deserializer");
    }

    @Override
    public ZombieReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        ZombieReportModel report = null;
        JsonObject object = json.getAsJsonObject();

        try {
            if (object.getAsJsonObject(ZombieReportModel.SERIALIZATION.SINGLE_KEY) != null) {
                object = object.getAsJsonObject(ZombieReportModel.SERIALIZATION.SINGLE_KEY);
            }
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
