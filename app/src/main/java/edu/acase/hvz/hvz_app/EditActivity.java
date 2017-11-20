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

class EditActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final LatLng latlng = (LatLng) getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText time = (EditText) findViewById(R.id.time);
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final String t = timeF.format(Calendar.getInstance().getTime());

        Button boton = (Button) findViewById(R.id.save);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MarkerOptions marker = new MarkerOptions().position(latlng);
                StringBuilder info = new StringBuilder();
                info.append(title.getText().toString());
                info.append(" human(s), ");
                time.setText(t);
                info.append(time.getText().toString());
                marker.title(info.toString());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("marker", marker);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}