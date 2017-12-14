package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/** Stunned is the state that zombies are in once they have been tagged by a human player.
 * This activity sets up the timer to show the time remaining while they are out of play,
 * waiting to become mobile again. */

public class StunnedActivity extends BaseActivity {
    TextView text1;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stunned);

        text1 = (TextView) findViewById(R.id.textView1);

        new CountDownTimer(60000, 1000) { // 8 seconds for testing. Can be changed later
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
                Intent i = new Intent(getApplicationContext(),ZombieActivity.class);
                startActivity(i);
                finish(); //prevent back button
            }
        }.start();
    }
}
