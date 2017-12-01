package edu.acase.hvz.hvz_app.api.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

public class HumanReportSerializer extends BaseReportSerializer<HumanReportModel> {
    @Override
    public JsonElement serialize(HumanReportModel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        JsonObject report = super.serialize(src, typeOfSrc, context).getAsJsonObject();

        //fields specific to HumanReportModel (super call handles the common ones in BaseReportModel)
        report.addProperty(HumanReportModel.SERIALIZATION.NUM_HUMANS, serialize(src.getNumHumans()));
        report.addProperty(HumanReportModel.SERIALIZATION.TYPICAL_MAG_SIZE, serialize(src.getTypicalMagSize()));

        root.add(HumanReportModel.SERIALIZATION.SINGLE_KEY, report);
        return root;
    }
}
