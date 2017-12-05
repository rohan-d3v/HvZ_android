package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;
class EditZ extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HumanReportRequest HumanReportRequest = new HumanReportRequest();
        List<HumanReportModel> zombieReports = HumanReportRequest.getAll();
        final LatLng latlng =  getIntent().getParcelableExtra("location");
        int id = 0;
        for (int i = 0; i < zombieReports.size(); i++){
            if (zombieReports.get(i).getLoc().toString().equals(latlng.toString()))
                id = i;
        }
        HumanReportModel val = zombieReports.get(id);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_z);

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText mag = (EditText) findViewById(R.id.mag);
        final EditText loc = (EditText) findViewById(R.id.lat);
        final long temp = Calendar.getInstance().getTimeInMillis();
        final Date timespot = Calendar.getInstance().getTime();

        Button boton = (Button) findViewById(R.id.save);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //title.setText(Integer.toString(val.getNumHumans()));
                //mag.setText(Integer.toString(val.getTypicalMagSize()));



                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        });
    }
}
