package edu.acase.hvz.hvz_app.reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import edu.acase.hvz.hvz_app.BaseActivity;
import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.R;
import edu.acase.hvz.hvz_app.ZombieActivity;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;

/** Create a HumanReport
 * @see HumanReportModel a HumanReport */

public class CreateHumanReportActivity extends BaseEditReportActivity {
    private static final Logger logger = new Logger("create_human_report");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();
    private HumanReportModel report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_human_report);

        // incoming from the zombie activity
        final LatLng marker = getIntent().getParcelableExtra("location");
        if (marker == null) {
            logger.error("location is null");
            return;
        }

        // fields on the view
        final EditText numHumans = (EditText) findViewById(R.id.groupSize);
        final EditText magSize = (EditText) findViewById(R.id.magazineSize);

        // button on the view
        final Button createButton = (Button) findViewById(R.id.saveButton);
        createButton.setOnClickListener(view -> {
            report = new HumanReportModel(1); //TODO: FIX ME

            int groupSize = tryParse(numHumans.getText().toString());
            int magazine = tryParse(magSize.getText().toString());
            if (groupSize >= 0)
                report.setNumHumans(groupSize);
            else
                report.setNumHumans(1);
            if (magazine >= 0)
                report.setTypicalMagSize(magazine);
            else
                report.setTypicalMagSize(1);

            report.setLocation(marker);
            report.setTimeSighted(new Date());
            report.setDatabase_id(humanReportRequest.create(report));

            Intent resultIntent = new Intent(getApplicationContext(), ZombieActivity.class);
            startActivity(resultIntent);
        });
    }
}
