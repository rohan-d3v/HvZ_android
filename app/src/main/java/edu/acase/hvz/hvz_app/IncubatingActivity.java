package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/** Incubating is the state that humans are in once they have been tagged by a zombie player.
 * This activity sets up the timer to show the time remaining while they are out of play,
 * waiting to become a zombie. */

public class IncubatingActivity extends AppCompatActivity {
    TextView text1;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubating);

        text1 = (TextView) findViewById(R.id.textView1);

        new CountDownTimer(2000, 1000) { // 8 seconds for testing. Can be changed later
            @Override
            public void onTick(long millisUntilFinished) {

                text1.setText(String.format(Locale.getDefault(), FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), ZombieActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        }.start();
    }
}
