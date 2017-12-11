package edu.acase.hvz.hvz_app.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


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
    protected final Logger logger = new Logger("edit_human_report");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_human_report);

        logger.debug("created edit activity");

        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final HumanReportModel report = mapMarker.getReport();

        final EditText numHumans = (EditText) findViewById(R.id.groupSize);
        numHumans.setText(String.valueOf(report.getNumHumans()));
        final EditText magSize = (EditText) findViewById(R.id.magazineSize);
        magSize.setText(String.valueOf(report.getTypicalMagSize()));

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int number = tryParse(numHumans.getText().toString());
                int magazine = tryParse(magSize.getText().toString());
                if (number >= 0 || magazine >= 0) {
                    if (number >= 0)
                        report.setNumHumans(number);
                    if (magazine >= 0)
                        report.setTypicalMagSize(magazine);
                    humanReportRequest.update(report);
                }

                Intent resultIntent = new Intent(getApplicationContext(), ZombieActivity.class);
                resultIntent.putExtra("mapMarker", mapMarker);
                resultIntent.putExtra("oldMarkerPosition", oldMarkerPosition);
                startActivity(resultIntent);
            }
        });
    }
}