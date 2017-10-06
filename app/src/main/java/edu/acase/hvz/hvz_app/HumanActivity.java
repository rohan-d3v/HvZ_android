package edu.acase.hvz.hvz_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HumanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human);
        final Button helpButton = (Button) findViewById(R.id.helpButton),
                     infoButton = (Button) findViewById(R.id.infoButton),
                     reportButton = (Button) findViewById(R.id.reportButton),
                     caughtButton = (Button) findViewById(R.id.caughtButton);

        final Context context = this.getBaseContext();

        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //onclick code
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //onclick code
            }
        });
        caughtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //onclick code

            }
        });
    }
}
