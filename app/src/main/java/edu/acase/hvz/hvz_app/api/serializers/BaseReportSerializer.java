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
            report.addProperty(ReportModel.SERIALIZATION.DATABASE_ID, src.getDATABASE_ID());
        report.addProperty(ReportModel.SERIALIZATION.GAME_ID, src.getGAME_ID());
        report.addProperty(ReportModel.SERIALIZATION.LOCATION_LAT, src.getLocation().latitude);
        report.addProperty(ReportModel.SERIALIZATION.LOCATION_LNG, src.getLocation().longitude);
        report.addProperty(ReportModel.SERIALIZATION.TIME_SIGHTED, serializeDate(src.getTimeSighted()));
        return report;
    }

    /* Note that the timezone calls will fail if 'AndroidThreeTen.init(this)' is not called by the current activity
     * Which is why it's in the onCreate() of BaseActivity.  */
    public static String serializeDate(Date date) {
        return DateTimeUtils.toInstant(date).atZone(ZoneId.systemDefault()).toOffsetDateTime().toString();
    }
}
