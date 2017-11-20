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

<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/editActivity.java
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class editActivity extends Activity {
=======
public class EditActivity extends Activity {
>>>>>>> 9d648bd2faf456be1f94cbc39a6f0e5b31bee9c4:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final LatLng latlng = getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
<<<<<<< HEAD:app/src/main/java/edu/acase/hvz/hvz_app/editActivity.java
        final EditText time = (EditText) findViewById(R.id.time);
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final String t = timeF.format(Calendar.getInstance().getTime());

        Button boton = (Button) findViewById(R.id.save);
        boton.setOnClickListener(new View.OnClickListener() {
=======
        Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
>>>>>>> 9d648bd2faf456be1f94cbc39a6f0e5b31bee9c4:app/src/main/java/edu/acase/hvz/hvz_app/EditActivity.java
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