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
import edu.acase.hvz.hvz_app.HumanActivity;
import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.R;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;

/** Create a ZombieReport
 * @see ZombieReportModel a ZombieReport */

public class CreateZombieReportActivity extends BaseEditReportActivity {
    protected final Logger logger = new Logger("create_zombie_report");
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
    private ZombieReportModel report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_zombie_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LatLng marker = getIntent().getParcelableExtra("location");
        final EditText numZombies = (EditText) findViewById(R.id.groupSize);

        final Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                report = new ZombieReportModel(1); //TODO: FIX ME

                int groupSize = tryParse(numZombies.getText().toString());
                if (groupSize >= 0)
                    report.setNumZombies(groupSize);
                else
                    report.setNumZombies(1);
                report.setLocation(marker);
                report.setTimeSighted(new Date());
                report.setDatabase_id(zombieReportRequest.create(report));

                Intent resultIntent = new Intent(getApplicationContext(), HumanActivity.class);
                startActivity(resultIntent);
            }
        });
    }
}
