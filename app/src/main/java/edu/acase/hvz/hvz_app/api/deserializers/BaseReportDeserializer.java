package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public abstract class BaseReportDeserializer<ReportModel extends BaseReportModel> implements JsonDeserializer<ReportModel>  {
    protected final String LOG_TAG;
    protected final Logger logger;

    protected BaseReportDeserializer(String LOG_TAG) {
        this.LOG_TAG = LOG_TAG;
        logger = new Logger(LOG_TAG);
    }

    @Override
    abstract public ReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException;

    public ReportModel deserialize(JsonElement json) throws JsonParseException {
        return deserialize(json, null, null);
    }

    public List<ReportModel> deserializeAll(JsonElement json, String ARRAY_KEY) throws JsonParseException {
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
            logger.error(e.getMessage());
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
