package edu.acase.hvz.hvz_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

/** The common dialogs between the player activities. This is for the help and info dialogs. */

public class CommonDialogs {
    public static void getHelpButtonDialog(Context context, View view) {
        AlertDialog.Builder builder = getAlertBuilder(context);
        builder.setTitle("Help")
                .setIcon(android.R.drawable.ic_menu_help)
                .setMessage("Usage info goes here")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //dismiss dialog
                })
                .show();
    }

    public static void getInfoButtonDialog(Context context, View view) {
        AlertDialog.Builder builder = getAlertBuilder(context);
        builder.setTitle("Info")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("Game info goes here")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //dismiss dialog
                })
                .show();
    }

    public static AlertDialog.Builder getAlertBuilder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        else
            return new AlertDialog.Builder(context);
    }
}
