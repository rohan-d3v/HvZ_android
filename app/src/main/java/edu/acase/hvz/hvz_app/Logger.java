package edu.acase.hvz.hvz_app;

import android.util.Log;

public class Logger {
    private final String LOG_TAG;

    public Logger(String tag) {
        LOG_TAG = tag;
    }

    public void debug(String... messages) {
        debug(LOG_TAG, false, messages);
    }
    public void debug(boolean multiline, String... messages) {
        debug(LOG_TAG, multiline, messages);
    }
    public static void debug(String tag, boolean multiline, String... messages) {
        Log.d(tag, getMessage(multiline, messages));
    }

    public void error(String... messages) {
        error(LOG_TAG, true, messages);
    }
    public void error(boolean multiline, String... messages) {
        error(LOG_TAG, multiline, messages);
    }
    public static void error(String tag, boolean multiline, String... messages) {
        Log.e(tag, getMessage(multiline, messages));
    }

    private static String getMessage(boolean multiline, String... messages) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i < messages.length; i++) {
            result.append(messages[i]);
            if (multiline && messages.length > 1 && i < messages.length - 1)
                result.append('\n');
        }
        return result.toString();
    }
}
