package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;

public class createH extends BaseActivity {
    private HumanReportModel nReport;

    private int tryParse(String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
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
                int number;
                number = tryParse(num.getText().toString());
                int magazine;
                magazine = tryParse(mag.getText().toString());

                HumanReportRequest request = new HumanReportRequest();
                nReport = new HumanReportModel(1);
                nReport.setLocation(marker);
                nReport.setTimeSighted(new Date());
                nReport.setNumHumans(number);
                nReport.setTypicalMagSize(magazine);
                nReport.setDatabase_id(3);
                //request.create(nReport);
                Intent resultIntent = new Intent(getApplicationContext(), ZombieActivity.class);
                startActivity(resultIntent);
            }

        });
    }

}
