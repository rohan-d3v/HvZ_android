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

/** The base deserializer to deserialize the json returned from server calls into models */

public abstract class BaseReportDeserializer<ReportModel extends BaseReportModel> implements JsonDeserializer<ReportModel>  {
    protected final String LOG_TAG;
    protected final Logger logger;

    protected BaseReportDeserializer(String LOG_TAG) {
        this.LOG_TAG = LOG_TAG;
        logger = new Logger(LOG_TAG);
    }

    /** Deserialize the json report from a server call into the java object form
     * @param json the report as json
     * @param type the type
     * @param context the deserialization context to use
     * @return the java object form of this report, or null if something went wrong
     */
    @Override
    abstract public ReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context);

    /** Deserialize the json report from a server call into the java object form
     * @param json the report as json
     * @return the java object form of this report, or null if something went wrong
     */
    public ReportModel deserialize(JsonElement json) {
        return deserialize(json, null, null);
    }

    /** Returns a list of all the reports from a server call that gets all of them as an array
     * @param json the json from the server
     * @param ARRAY_KEY the key used to demarcate the array {ARRAY_KEY: [{report}, {report}, ...]}
     * @return the list of all reports after being deserialized
     */
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
            logger.error(e.getMessage());
        }
        return reports;
    }

    /** Deserialize the date from the server into a java-style date
     * @param jsonDateElement server-style date (iso 8601) as json
     * @return jave-usable date
     */
    public Date deserializeDate(JsonElement jsonDateElement) {
        return deserializeDate(jsonDateElement.getAsString());
    }

    /** Deserialize the date from the server into a java-style date
     * @param jsonDateString server-style date (iso 8601) as string
     * @return jave-usable date
     */
    public Date deserializeDate(String jsonDateString) {
        OffsetDateTime odt = OffsetDateTime.parse(jsonDateString);
        return DateTimeUtils.toDate(odt.toInstant());
    }
}
