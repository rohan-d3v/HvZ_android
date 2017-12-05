package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
import java.util.Objects;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

class EditActivity extends BaseActivity {
    protected final Logger logger = new Logger("EditActivity");
=======
import java.util.HashMap;
import java.util.Map;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

class EditH extends BaseActivity {
    private Map<MarkerOptions, MapMarker> markerMap = new HashMap<>();
>>>>>>> origin/master:app/src/main/java/edu/acase/hvz/hvz_app/EditH.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_h);

<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
        logger.debug("created edit activity");

        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MarkerOptions markerOptions = mapMarker.getMarkerOptions();
        final BaseReportModel report = mapMarker.getReport();
=======
        final LatLng latlng =  getIntent().getParcelableExtra("location");
>>>>>>> origin/master:app/src/main/java/edu/acase/hvz/hvz_app/EditH.java

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText loc = (EditText) findViewById(R.id.lat);
        final long currentTime = Calendar.getInstance().getTimeInMillis();
        final Date currentDate = Calendar.getInstance().getTime();

        //autofill form
        time.setText(report.getTimeSighted().toString());
        loc.setText(report.getLocation().toString());
        if (report instanceof HumanReportModel) {
            title.setText(String.valueOf(((HumanReportModel) report).getNumHumans()));
            mag.setText(String.valueOf(((HumanReportModel) report).getTypicalMagSize()));
        }
        else if (report instanceof ZombieReportModel) {
            title.setText(String.valueOf(((ZombieReportModel) report).getNumZombies()));
        }

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
                logger.debug(true, "raw mapMarker: ", mapMarker.toString());

                int number, magazine;
                number = tryParse(title.getText().toString());//number of humans
                magazine = tryParse(mag.getText().toString());//number of darts  in mag
                //time since last update
                CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(report.getTimeSighted().getTime(), System.currentTimeMillis(), SECOND_IN_MILLIS);
=======
                //MarkerOptions marker = new MarkerOptions().position(latlng);
                time.setText(timespot.toString());//autofill time

                int number;
                number = Integer.parseInt(title.getText().toString());//number of zombies
                //time since last update
                CharSequence s = DateUtils.getRelativeTimeSpanString(temp,  (System.currentTimeMillis()), SECOND_IN_MILLIS);
>>>>>>> origin/master:app/src/main/java/edu/acase/hvz/hvz_app/EditH.java

                if (report instanceof HumanReportModel) {
                    HumanReportModel humanReport = (HumanReportModel) report;
                    if (number >= 0) humanReport.setNumHumans(number);
                    if (magazine >= 0) humanReport.setTypicalMagSize(magazine);
                    mapMarker.setReport(humanReport);

<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
                } else if (report instanceof ZombieReportModel) {
                    ZombieReportModel zombieReport = (ZombieReportModel) report;
                    if (number >= 0) zombieReport.setNumZombies(number);
                    mapMarker.setReport(zombieReport);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("mapMarker", mapMarker);
                resultIntent.putExtra("oldMarkerPosition", oldMarkerPosition);
=======
                //marker.title("  ");
                Intent resultIntent = new Intent();
>>>>>>> origin/master:app/src/main/java/edu/acase/hvz/hvz_app/EditH.java
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        });
    }

<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
    private int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    //what is this for?
    public String setT (MarkerOptions marker, long temp){
        return DateUtils.getRelativeTimeSpanString(temp, (long) (System.currentTimeMillis()), SECOND_IN_MILLIS).toString();
    }
=======
>>>>>>> origin/master:app/src/main/java/edu/acase/hvz/hvz_app/EditH.java
}