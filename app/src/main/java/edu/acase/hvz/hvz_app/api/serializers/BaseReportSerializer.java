package edu.acase.hvz.hvz_app.api.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;

import java.lang.reflect.Type;
import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public abstract class BaseReportSerializer<ReportModel extends BaseReportModel> implements JsonSerializer<ReportModel> {
    @Override
    public JsonElement serialize(ReportModel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject report = new JsonObject();
        if (src.getDATABASE_ID() >= 0)
            report.addProperty(ReportModel.SERIALIZATION.DATABASE_ID, serialize(src.getDATABASE_ID()));
        report.addProperty(ReportModel.SERIALIZATION.GAME_ID, serialize(src.getGAME_ID()));
        report.addProperty(ReportModel.SERIALIZATION.LOCATION_LAT, serialize(src.getLocation().latitude));
        report.addProperty(ReportModel.SERIALIZATION.LOCATION_LNG, serialize(src.getLocation().longitude));
        report.addProperty(ReportModel.SERIALIZATION.TIME_SIGHTED, serialize(src.getTimeSighted()));
        return report;
    }

    /* Note that the timezone calls will fail if 'AndroidThreeTen.init(this)' is not called by the current activity
     * Which is why it's in the onCreate() of BaseActivity.  */
    public static String serialize(Date date) {
        return DateTimeUtils.toInstant(date).atZone(ZoneId.systemDefault()).toOffsetDateTime().toString();
    }

    //all these serializers are for the apipie validations on the server side
    //see "http://35.163.170.184/api" update/create methods for more details

    public static String serialize(double value) {
        return String.valueOf(value);
    }

    public static String serialize(float value) {
        return String.valueOf(value);
    }

    public static int serialize(int value) {
        return value;
    }

    public static String serialize(char value) {
        return String.valueOf(value);
    }
}
