package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


/** This activity shows the human player their code so that they can reveal it to
 * zombies whenever they are tagged. Zombies can enter or scan this code, which will
 * cause the human to turn into a zombie (they will first incubate). */

public class ViewCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String code = getToken();

        TextView viewC = (TextView) findViewById(R.id.textView);
        viewC.setText(code);
        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), IncubatingActivity.class);
            startActivity(intent);
        });
    }


    public String getToken() {
        Random random = new Random();
        String CHARS = "ABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
        StringBuilder token = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }
}
