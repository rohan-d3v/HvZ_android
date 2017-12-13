package edu.acase.hvz.hvz_app.api.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

/** Serializer used to turn ZombieReportModels from the java side into server-formatted json objects
 * @see BaseReportSerializer the base report serializer */

public class ZombieReportSerializer extends BaseReportSerializer<ZombieReportModel> {
    /** Serialize the java report object into a json element
     * @param src the java report object
     * @param typeOfSrc the type
     * @param context the context
     * @return the json element
     */
    @Override
    public JsonElement serialize(ZombieReportModel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        JsonObject report = super.serialize(src, typeOfSrc, context).getAsJsonObject();

        //fields specific to ZombieReportModel (super call handles the common ones in BaseReportModel)
        report.addProperty(ZombieReportModel.SERIALIZATION.NUM_ZOMBIES, serialize(src.getNumZombies()));

        root.add(ZombieReportModel.SERIALIZATION.SINGLE_KEY, report);
        return root;
    }
}
