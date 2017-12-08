package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;


class EditZ extends BaseActivity {
    protected final Logger logger = new Logger("EditActivity");
    private static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_z);

        logger.debug("Created edit activity");

        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final ZombieReportModel report = (ZombieReportModel) mapMarker.getReport();

        final EditText numZombies = (EditText) findViewById(R.id.title);
        numZombies.setText(String.valueOf(report.getNumZombies()));

        Button saveButton = (Button) findViewById(R.id.save);
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

    private int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

}