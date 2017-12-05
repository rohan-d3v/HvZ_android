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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

class EditZ extends BaseActivity {
    protected final Logger logger = new Logger("EditActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_z);

        logger.debug("created edit activity");

        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        final MarkerOptions markerOptions = mapMarker.getMarkerOptions();
        final BaseReportModel report = mapMarker.getReport();

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText mag = (EditText) findViewById(R.id.mag);
        final EditText loc = (EditText) findViewById(R.id.lat);
        final long currentTime = Calendar.getInstance().getTimeInMillis();
        final Date currentDate = Calendar.getInstance().getTime();

        //autofill form
        time.setText(report.getTimeSighted().toString());
        loc.setText(report.getLocation().toString());
        if (report instanceof HumanReportModel) {
            title.setText(String.valueOf(((HumanReportModel) report).getNumHumans()));
            mag.setText(String.valueOf(((HumanReportModel) report).getTypicalMagSize()));
        }
        else if (report instanceof ZombieReportModel) {
            title.setText(String.valueOf(((ZombieReportModel) report).getNumZombies()));
        }

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                logger.debug(true, "raw mapMarker: ", mapMarker.toString());

                int number, magazine;
                number = tryParse(title.getText().toString());//number of humans
                magazine = tryParse(mag.getText().toString());//number of darts  in mag
                //time since last update
                CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(report.getTimeSighted().getTime(), System.currentTimeMillis(), SECOND_IN_MILLIS);

                if (report instanceof HumanReportModel) {
                    HumanReportModel humanReport = (HumanReportModel) report;
                    if (number >= 0) humanReport.setNumHumans(number);
                    if (magazine >= 0) humanReport.setTypicalMagSize(magazine);
                    mapMarker.setReport(humanReport);

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

    //what is this for?
    public String setT (MarkerOptions marker, long temp){
        return DateUtils.getRelativeTimeSpanString(temp, (long) (System.currentTimeMillis()), SECOND_IN_MILLIS).toString();
    }
}