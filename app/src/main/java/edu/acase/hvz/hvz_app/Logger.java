package edu.acase.hvz.hvz_app;

import android.util.Log;

/** This is a utility class for activities or other files to use to log messages
 * to Logcat instead of using stdout/stderr. */

public class Logger {
    private final String LOG_TAG;

    public Logger(String tag) {
        LOG_TAG = tag;
    }

    /** Send several messages as debugs. These will be shown all as one line.
     * @param messages the messages to send out
     * */
    public void debug(String... messages) {
        debug(LOG_TAG, false, messages);
    }

    /** Send several messages as debugs. The caller can specify whether or not
     * to use linebreaks between messages
     * @param multiline true = line break between messages, false = all on one line
     * @param messages the messages to send out
     * */
    public void debug(boolean multiline, String... messages) {
        debug(LOG_TAG, multiline, messages);
    }

    /** Send several messages as debugs. The caller can specify whether or not
     * to use linebreaks between messages
     * @param tag the tag to use in Logcat
     * @param multiline true = line break between messages, false = all on one line
     * @param messages the messages to send out
     * */
    public static void debug(String tag, boolean multiline, String... messages) {
        Log.d(tag, getMessage(multiline, messages));
    }

    /** Send several messages as errors. These will be shown all as one line.
     * @param messages the messages to send out
     * */
    public void error(String... messages) {
        error(LOG_TAG, false, messages);
    }

    /** Send several messages as errors. The caller can specify whether or not
     * to use linebreaks between messages
     * @param multiline true = line break between messages, false = all on one line
     * @param messages the messages to send out
     * */
    public void error(boolean multiline, String... messages) {
        error(LOG_TAG, multiline, messages);
    }

    /** Send several messages as errors. The caller can specify whether or not
     * to use linebreaks between messages
     * @param tag the tag to use in Logcat
     * @param multiline true = line break between messages, false = all on one line
     * @param messages the messages to send out
     * */
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
