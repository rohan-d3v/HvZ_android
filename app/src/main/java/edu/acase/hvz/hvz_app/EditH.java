package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;


class EditH extends BaseActivity {
    protected final Logger logger = new Logger("EditActivity");
    private ZombieReportModel dummyReport;
    private ZombieReportModel newR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_h);

        logger.debug("created edit activity");

        final MapMarker mapMarker = getIntent().getParcelableExtra("mapMarker");
        final LatLng oldMarkerPosition = getIntent().getParcelableExtra("oldMarkerPosition");
        ZombieReportRequest request = new ZombieReportRequest();
        //final BaseReportModel report = mapMarker.getReport();
        List<ZombieReportModel> val = request.getAll();
        for (int z = 0; z < val.size(); z++){
            LatLng temp = val.get(z).getLocation();
            if (temp.toString().equals(oldMarkerPosition.toString()))
                dummyReport = val.get(z);
        }

        final EditText title = (EditText) findViewById(R.id.title);
        title.setText(String.valueOf(dummyReport.getNumZombies()));


        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //logger.debug(true, "raw mapMarker: ", mapMarker.toString());

                int number;
                number = tryParse(title.getText().toString());//number of zombies
                ZombieReportRequest temp = new ZombieReportRequest();
                newR = new ZombieReportModel(1);
                newR.setLocation(oldMarkerPosition);
                newR.setTimeSighted(new Date());
                newR.setNumZombies(number);
                newR.setDatabase_id(temp.create(newR));

                temp.delete(dummyReport);

                Intent resultIntent = new Intent(getApplicationContext(), HumanActivity.class);
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