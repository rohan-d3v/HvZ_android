package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;

class EditH extends BaseActivity {
    protected final Logger logger = new Logger("EditH");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_h);

        logger.debug("created edit activity");

        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final HumanReportModel report = (HumanReportModel) mapMarker.getReport();

        final EditText numHumans = (EditText) findViewById(R.id.title);
        numHumans.setText(String.valueOf(report.getNumHumans()));
        final EditText magSize = (EditText) findViewById(R.id.mag);
        magSize.setText(String.valueOf(report.getTypicalMagSize()));

        Button saveButton = (Button) findViewById(R.id.save);
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

    private int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}