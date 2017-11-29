package edu.acase.hvz.hvz_app.api.deserializers;

import com.google.gson.JsonElement;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.OffsetDateTime;

import java.util.Date;

public abstract class BaseReportDeserializer {
    public Date deserializeDate(JsonElement jsonDateElement) {
        return deserializeDate(jsonDateElement.getAsString());
    }
    public Date deserializeDate(String jsonDateString) {
        OffsetDateTime odt = OffsetDateTime.parse(jsonDateString);
        return DateTimeUtils.toDate(odt.toInstant());
    }
}
