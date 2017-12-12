package edu.acase.hvz.hvz_app.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.fail;

abstract class BaseReportSerialization {
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    protected static Date stringToDate(String dateString) {
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return date;
    }

    protected static String dateToString(Date date) {
        return df.format(date);
    }
}
