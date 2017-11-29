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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static android.text.format.DateUtils.SECOND_IN_MILLIS;

class EditActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final LatLng latlng = (LatLng) getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText mag = (EditText) findViewById(R.id.mag);
        final EditText loc = (EditText) findViewById(R.id.lat);
        final String t = Calendar.getInstance().getTime().toString();
        final long temp = Calendar.getInstance().getTimeInMillis();

        Button boton = (Button) findViewById(R.id.save);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MarkerOptions marker = new MarkerOptions().position(latlng);
                time.setText(t);

                StringBuilder info = new StringBuilder();
                double number, magazine, lat, lng;
                number = Double.parseDouble(title.getText().toString());
                magazine = Double.parseDouble(mag.getText().toString());
                lat = latlng.latitude;
                lng = latlng.longitude;
                time.setText(t);

                info.append(number);
                info.append(", ");

                CharSequence s = DateUtils.getRelativeTimeSpanString(temp, (long) (System.currentTimeMillis()), SECOND_IN_MILLIS);
                info.append(s);
                info.append(",");
                info.append(t);
                info.append(", ");

                loc.setText(latlng.toString());
                info.append(lat);
                info.append(", ");
                info.append(lng);

                marker.title(s.toString());


                Intent resultIntent = new Intent();
                resultIntent.putExtra("marker", marker);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        });
    }
}