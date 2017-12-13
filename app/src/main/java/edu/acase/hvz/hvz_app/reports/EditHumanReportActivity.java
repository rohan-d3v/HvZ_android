package edu.acase.hvz.hvz_app.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import java.util.Date;

import edu.acase.hvz.hvz_app.BaseActivity;
import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.MapMarker;
import edu.acase.hvz.hvz_app.R;
import edu.acase.hvz.hvz_app.ZombieActivity;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;

/** Edit a HumanReport
 * @see HumanReportModel a HumanReport */

public class EditHumanReportActivity extends BaseEditReportActivity {
    private static final Logger logger = new Logger("edit_human_report");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_human_report);

        logger.debug("created edit activity");

        // incoming from zombie activity
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        if (mapMarker == null) {
            logger.error("mapMarker is null");
            return;
        }
        final HumanReportModel report = mapMarker.getReport();

        // fields on the view
        final EditText numHumans = (EditText) findViewById(R.id.groupSize);
        numHumans.setText(String.valueOf(report.getNumHumans()));
        final EditText magSize = (EditText) findViewById(R.id.magazineSize);
        magSize.setText(String.valueOf(report.getTypicalMagSize()));

        // save button on the view
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            int number = tryParse(numHumans.getText().toString());
            int magazine = tryParse(magSize.getText().toString());
            if (number >= 0 || magazine >= 0) {
                if (number >= 0)
                    report.setNumHumans(number);
                if (magazine >= 0)
                    report.setTypicalMagSize(magazine);

                report.setTimeSighted(new Date());
                humanReportRequest.update(report);
            }

            Intent resultIntent = new Intent(getApplicationContext(), ZombieActivity.class);
            //resultIntent.putExtra("mapMarker", mapMarker); //for android testing
            startActivity(resultIntent);
        });
    }
}