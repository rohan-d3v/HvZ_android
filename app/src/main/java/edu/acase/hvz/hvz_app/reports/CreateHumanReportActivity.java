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

public class CreateHumanReportActivity extends BaseActivity {
    protected final Logger logger = new Logger("create_human_report");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();
    private HumanReportModel report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_h);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LatLng marker = getIntent().getParcelableExtra("location");
        final EditText num = (EditText) findViewById(R.id.title);
        final EditText mag = (EditText) findViewById(R.id.mag);

        final Button Create = (Button) findViewById(R.id.save);
        Create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                report = new HumanReportModel(1); //TODO: FIX ME

                int number = tryParse(num.getText().toString());
                int magazine = tryParse(mag.getText().toString());
                if (number >= 0)
                    report.setNumHumans(number);
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
            }

        });
    }

    private int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
