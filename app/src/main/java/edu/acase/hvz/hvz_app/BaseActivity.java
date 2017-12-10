package edu.acase.hvz.hvz_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;

public abstract class BaseActivity extends AppCompatActivity {
    /* Note that timezone calls will fail if 'AndroidThreeTen.init(this)' is not called by the current activity
     * Which is why it's in the onCreate() of BaseActivity.  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
    }
}
