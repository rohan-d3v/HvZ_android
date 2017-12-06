package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;


class EditH extends BaseActivity {
    protected final Logger logger = new Logger("EditActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_h);

        logger.debug("created edit activity");

        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final BaseReportModel report = mapMarker.getReport();

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText loc = (EditText) findViewById(R.id.lat);

        //autofill form
        time.setText(report.getTimeSighted().toString());
        loc.setText(report.getLocation().toString());
        if (report instanceof ZombieReportModel) {
            title.setText(String.valueOf(((ZombieReportModel) report).getNumZombies()));
        }


        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                logger.debug(true, "raw mapMarker: ", mapMarker.toString());

                int number;
                number = tryParse(title.getText().toString());//number of zombies


                if (report instanceof ZombieReportModel) {
                    ZombieReportModel zombieReport = (ZombieReportModel) report;
                    if (number >= 0) zombieReport.setNumZombies(number);
                    mapMarker.setReport(zombieReport);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("mapMarker", mapMarker);
                resultIntent.putExtra("oldMarkerPosition", oldMarkerPosition);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
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