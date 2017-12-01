package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public abstract class BaseReportDeserializer<ReportModel extends BaseReportModel> implements JsonDeserializer<ReportModel>  {
    @Override
    abstract public ReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context);

    public List<ReportModel> deserializeAll(JsonElement json, String ARRAY_KEY) {
        List<ReportModel> reports = new ArrayList<>();
        JsonObject root = json.getAsJsonObject();

        try {
            if (root.has(ARRAY_KEY)) {
                JsonArray jsonArray = root.getAsJsonArray(ARRAY_KEY);
                for (JsonElement jsonElement : jsonArray) {
                    ReportModel report = deserialize(jsonElement, null, null);
                    if (report != null)
                        reports.add(report);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public Date deserializeDate(JsonElement jsonDateElement) {
        return deserializeDate(jsonDateElement.getAsString());
    }

    public Date deserializeDate(String jsonDateString) {
        OffsetDateTime odt = OffsetDateTime.parse(jsonDateString);
        return DateTimeUtils.toDate(odt.toInstant());
    }
}
