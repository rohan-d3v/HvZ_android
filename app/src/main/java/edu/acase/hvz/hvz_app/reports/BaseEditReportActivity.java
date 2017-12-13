package edu.acase.hvz.hvz_app.reports;

import edu.acase.hvz.hvz_app.BaseActivity;

/** Base class for all report edit/create activities */

public abstract class BaseEditReportActivity extends BaseActivity {

    /** Try to parse the int from a string
     * @param string input string
     * @return the integer representation, or -1 if it fails
     */
    public static int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
