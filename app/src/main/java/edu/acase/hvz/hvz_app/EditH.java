package edu.acase.hvz.hvz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

class EditH extends BaseActivity {
    private Map<MarkerOptions, MapMarker> markerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_h);

        final LatLng latlng =  getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText loc = (EditText) findViewById(R.id.lat);
        final long temp = Calendar.getInstance().getTimeInMillis();
        final Date timespot = Calendar.getInstance().getTime();

        Button boton = (Button) findViewById(R.id.save);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //MarkerOptions marker = new MarkerOptions().position(latlng);
                time.setText(timespot.toString());//autofill time

                int number;
                number = Integer.parseInt(title.getText().toString());//number of zombies
                //time since last update
                CharSequence s = DateUtils.getRelativeTimeSpanString(temp,  (System.currentTimeMillis()), SECOND_IN_MILLIS);


                //marker.title("  ");
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        });
    }

}