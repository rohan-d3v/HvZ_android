package edu.acase.hvz.hvz_app.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import java.util.Date;

import edu.acase.hvz.hvz_app.BaseActivity;
import edu.acase.hvz.hvz_app.HumanActivity;
import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.MapMarker;
import edu.acase.hvz.hvz_app.R;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

/** Edit a ZombieReport
 * @see ZombieReportModel a ZombieReport */

public class EditZombieReportActivity extends BaseEditReportActivity {
    private static final Logger logger = new Logger("edit_zombie_report");
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_zombie_report);

        logger.debug("Created edit activity");

        // incoming from zombie activity
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        if (mapMarker == null) {
            logger.error("MapMarker is null");
            return;
        }
        final ZombieReportModel report = mapMarker.getReport();

        // fields on the view
        final EditText numZombies = (EditText) findViewById(R.id.groupSize);
        numZombies.setText(String.valueOf(report.getNumZombies()));

        // save button on the view
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            int number = tryParse(numZombies.getText().toString());
            if (number >= 0) {
                report.setNumZombies(number);
                report.setTimeSighted(new Date());
                zombieReportRequest.update(report);
            }
            Intent resultIntent = new Intent(getApplicationContext(), HumanActivity.class);
            //resultIntent.putExtra("mapMarker", mapMarker); //for android testing
            startActivity(resultIntent);
        });
    }
}