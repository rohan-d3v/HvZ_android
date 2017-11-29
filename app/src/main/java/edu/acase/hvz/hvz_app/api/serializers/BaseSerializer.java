package edu.acase.hvz.hvz_app.api.serializers;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;

import java.util.Date;

public class BaseSerializer {
    /* Note that the timezone calls will fail if 'AndroidThreeTen.init(this)' is not called by the current activity
     * Which is why it's in the onCreate() of BaseActivity.  */
    public static String serializeDate(Date date) {
        return DateTimeUtils.toInstant(date).atZone(ZoneId.systemDefault()).toOffsetDateTime().toString();
    }
}
