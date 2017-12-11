package edu.acase.hvz.hvz_app.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


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
    protected final Logger logger = new Logger("edit_zombie_report");
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_zombie_report);

        logger.debug("Created edit activity");

        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final ZombieReportModel report = mapMarker.getReport();

        final EditText numZombies = (EditText) findViewById(R.id.groupSize);
        numZombies.setText(String.valueOf(report.getNumZombies()));

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int number = tryParse(numZombies.getText().toString());
                if (number >= 0) {
                    report.setNumZombies(number);
                    zombieReportRequest.update(report);
                }
                Intent resultIntent = new Intent(getApplicationContext(), HumanActivity.class);
                resultIntent.putExtra("mapMarker", mapMarker);
                resultIntent.putExtra("oldMarkerPosition", oldMarkerPosition);
                startActivity(resultIntent);
            }

        });
    }
}