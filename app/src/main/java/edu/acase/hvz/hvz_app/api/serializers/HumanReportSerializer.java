package edu.acase.hvz.hvz_app.api.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

/** Serializer used to turn HumanReportModels from the java side into server-formatted json objects
 * @see BaseReportSerializer the base report serializer */

public class HumanReportSerializer extends BaseReportSerializer<HumanReportModel> {
    /** Serialize the java report object into a json element
     * @param src the java report object
     * @param typeOfSrc the type
     * @param context the context
     * @return the json element
     */
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
