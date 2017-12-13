package edu.acase.hvz.hvz_app;


import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

import edu.acase.hvz.hvz_app.HumanActivity;
import edu.acase.hvz.hvz_app.MapMarker;
import edu.acase.hvz.hvz_app.R;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.reports.BaseEditReportActivity;
import edu.acase.hvz.hvz_app.reports.EditHumanReportActivity;
import edu.acase.hvz.hvz_app.reports.EditZombieReportActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditZombieReportTest {

    @Rule
    public ActivityTestRule<HumanActivity> mActivityTestRule = new ActivityTestRule<>(HumanActivity.class);

    @Test
    public void editZombieReportTest() throws Exception {
        int executionDelayMs = 1000;
        // Added a sleep statement to match the app's execution delay.
        Thread.sleep(executionDelayMs);
        final HumanActivity thisActivity = mActivityTestRule.getActivity();

        //create a report
        ZombieReportModel report = new ZombieReportModel(999, 64);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        report.setTimeSighted(calendar.getTime());
        final int groupSize = 11;
        report.setNumZombies(groupSize);
        report.setLocation(thisActivity.cwruQuad);
        System.out.println("generated report: \n" + report.toString());

        //create a report marker
        final Semaphore mutex = new Semaphore(0);
        thisActivity.runOnUiThread(() -> {
            MapMarker mapMarker = new MapMarker(report);
            mapMarker.getMarkerOptions().snippet(report.getReportContents());
            System.out.println("marker snippet: " + mapMarker.getMarkerOptions().getSnippet());
            Marker marker = thisActivity.gmap.addMarker(mapMarker.getMarkerOptions());
            thisActivity.markerMap.put(marker, mapMarker);
            System.out.println("added marker: \n " + marker.toString() + "\n   snippet: " + marker.getSnippet());
            mutex.release();
        });
        mutex.acquire();

        //click on the report marker
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(report.getReportContents()));
        marker.click();

        //click on the edit button
        UiObject editMarkerButton = device.findObject(new UiSelector().clickable(true).checkable(false).text("Edit"));
        editMarkerButton.clickAndWaitForNewWindow();

        //the activity should have switched to the EditReportActivity

        //verify the group size is correct
        UiObject groupSizeObject = device.findObject(new UiSelector().focusable(true).text(String.valueOf(groupSize)));
        assertTrue(groupSizeObject.exists());

        //set a new group size
        String newGroupSize = "888";
        ViewInteraction groupSizeInteraction = onView(Matchers.allOf(ViewMatchers.withId(R.id.groupSize), isDisplayed()));
        groupSizeInteraction.perform(
                click(),
                replaceText(newGroupSize),
                closeSoftKeyboard());

        //verify the new group size is set
        groupSizeObject = device.findObject(new UiSelector().focusable(true).text(String.valueOf(newGroupSize)));
        assertTrue(groupSizeObject.exists());

        //save the edited report
        ViewInteraction saveButton = onView(allOf(withId(R.id.saveButton), isDisplayed()));
        saveButton.perform(click());

        thisActivity.runOnUiThread(() -> {
            thisActivity.logger.error("extras in "+ thisActivity.getLocalClassName());
            thisActivity.logger.error(thisActivity.getParentActivityIntent()+"");
            thisActivity.logger.error(thisActivity.getCallingActivity()+"");
            thisActivity.logger.error(thisActivity.getIntent()+"");
            thisActivity.logger.error(thisActivity.getIntent().getExtras()+"");
            mutex.release();
        });
        mutex.acquire();


        System.out.println(thisActivity.getIntent());
        System.out.println(thisActivity.getIntent().getExtras());
        System.out.println(thisActivity.getParentActivityIntent());
        System.out.println(thisActivity.getSupportParentActivityIntent());
        if (thisActivity.getIntent().getExtras() != null)
            thisActivity.logger.error(thisActivity.getIntent().getExtras().toString());

        //wait for the gmap to re-populate
        //Thread.sleep(executionDelayMs);

        //check that the updated marker is on the map
        //String newMarkerContents = "Num Zombies = "+newGroupSize;
        //marker = device.findObject(new UiSelector().descriptionContains(newMarkerContents));
        //assertTrue(marker.exists());
    }
}
