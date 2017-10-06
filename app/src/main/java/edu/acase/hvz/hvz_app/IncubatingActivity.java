package edu.acase.hvz.hvz_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IncubatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubating);
        //fetch countdown time remaining (in db, add a "zombieTime" date field or similar)
        //run countdown timer
        //occasionally synchronize timer
        //if timer is finished, transition to zombieActivity
    }
}
