package edu.acase.hvz.hvz_app;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.android.gms.maps.model.Marker;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ZombieReportTest {
    @Rule
    public ActivityTestRule<HumanActivity> mActivityTestRule = new ActivityTestRule<>(HumanActivity.class);
    HumanActivity thisActivity;
    final int executionDelayMs = 500;

    final Logger logger = new Logger("zombie_report_test");

    final ZombieReportRequest request = new ZombieReportRequest();
    ZombieReportModel report;
    final int groupSize = 11;

    @Test
    public void testZombieReport() throws Exception {
        Thread.sleep(executionDelayMs); //set-up time for app
        thisActivity = mActivityTestRule.getActivity();

        assertTrue(createZombieReport());
        assertTrue(editZombieReport());

        Thread.sleep(executionDelayMs); //wait for gmap to repopulate
        assertTrue(deleteZombieReport());
    }

    public boolean createZombieReport() throws Exception {
        //create a report
        report = new ZombieReportModel(1);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        report.setTimeSighted(calendar.getTime());
        report.setNumZombies(groupSize);
        report.setLocation(thisActivity.cwruQuad);
        logger.debug("generated report: \n" + report.toString());

        //delete any old matching markers
        try {

            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents().split("\n")[0]));
            while(marker.exists()) {
                marker.click();

                //delete the marker
                UiObject deleteMarkerButton = device.findObject(new UiSelector().clickable(true).checkable(false).text("Delete"));
                assertTrue(deleteMarkerButton.exists());
                deleteMarkerButton.click();

                Thread.sleep(100);
                marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents().split("\n")[0]));
            }
        } catch (Exception e) {
            logger.error(true, "no old marker found", e.getMessage());
        }

        //create a report marker
        final Semaphore mutex = new Semaphore(0);
        thisActivity.runOnUiThread(() -> {
            MapMarker mapMarker = new MapMarker(report);
            Marker marker = thisActivity.gmap.addMarker(mapMarker.getMarkerOptions());
            thisActivity.markerMap.put(marker, mapMarker);
            logger.debug("added marker: \n " + marker.toString() + "\n   snippet: " + marker.getSnippet());
            mutex.release();
        });
        mutex.acquire();

        //upload to server
        report.setDatabase_id(request.create(report));
        return true;
    }

    public boolean editZombieReport() throws Exception {
        //click on the report marker
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents()));
        assertTrue(marker.exists());
        marker.click();

        //click on the edit button
        UiObject editMarkerButton = device.findObject(new UiSelector().clickable(true).checkable(false).text("Edit"));
        assertTrue(editMarkerButton.exists());
        editMarkerButton.clickAndWaitForNewWindow();

        //the activity should have switched to the EditReportActivity

        //verify the group size is correct
        UiObject groupSizeObject = device.findObject(new UiSelector().focusable(true).text(String.valueOf(groupSize)));
        assertTrue(groupSizeObject.exists());

        //set a new group size
        int newGroupSize = 888;
        ViewInteraction groupSizeInteraction = onView(withId(R.id.groupSize));
        groupSizeInteraction.check(matches(isDisplayed()));
        groupSizeInteraction.perform(
                click(),
                replaceText(""+newGroupSize),
                closeSoftKeyboard());

        //verify the new group size is set
        groupSizeObject = device.findObject(new UiSelector().focusable(true).text(String.valueOf(newGroupSize)));
        assertTrue(groupSizeObject.exists());

        //mirror updates to object for comparisons
        report.setNumZombies(newGroupSize);
        report.setTimeSighted(new Date());

        //save the edited report
        ViewInteraction saveButton = onView(withId(R.id.saveButton));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        return true;
    }

    public boolean deleteZombieReport() throws Exception {
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        //click the updated marker on the map
        UiObject marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents()));
        assertTrue(marker.exists());
        marker.click();

        //delete the marker
        UiObject deleteMarkerButton = device.findObject(new UiSelector().clickable(true).checkable(false).text("Delete"));
        assertTrue(deleteMarkerButton.exists());
        deleteMarkerButton.click();

        Thread.sleep(500);

        //verify that it's gone
        marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents()));
        assertFalse(marker.exists());
        return true;
    }
}
