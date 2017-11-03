package edu.acase.hvz.hvz_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IncubatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubating);
        //fetch countdown time remaining (in db, add a "zombieTime" date field or similar)
        //run countdown timer
        //occasionally synchronize timer
        //if timer is finished, transition to zombieActivity

        final Button zombieButton = (Button) findViewById(R.id.zombieButton);
        final Context context = this;
        zombieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), ZombieActivity.class);
                startActivity(goToNextActivity);
                finish(); //prevent back button
            }
        });
    }
}
