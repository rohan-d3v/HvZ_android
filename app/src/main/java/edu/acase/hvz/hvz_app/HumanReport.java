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
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HumanReport extends AppCompatActivity {
    final MapMarker marker;

    public HumanReport(MapMarker marker) {
        this.marker = marker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humanreport);

        final LatLng latlng = (LatLng) getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText loc = (EditText)  findViewById(R.id.lat);
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final String t = timeF.format(Calendar.getInstance().getTime());


        Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MarkerOptions marker = new MarkerOptions().position(latlng);
                StringBuilder info = new StringBuilder();
                double number, magazine, lat, lng;
                number = Double.parseDouble(title.getText().toString());
                lat = latlng.latitude;
                lng = latlng.longitude;

                info.append(number);
                info.append(", ");

                time.setText(t);
                info.append(time.getText().toString());
                info.append(", ");

                loc.setText(latlng.toString());
                info.append(lat);
                info.append(", ");
                info.append(lng);

                marker.title(info.toString());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("marker", marker);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
