package edu.acase.hvz.hvz_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;

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
        WebView webView = new WebView(context);
        webView.loadData("<p>Report: Report player, form opens up, input data, press save</p>\n" +
                "<p>OnMarker long click: for edit, refer to report, for move, opens new activity, for delete, deletes marker</p>\n" +
                "<p>Stunned/ Incubating: Use when the opposite player gets you. Stuns or turns you into a zombie accordingly</p>\n" +
                "<p>the heatmap displays hordes if you're a zombie. join them and hunt</p>", "text/html", "utf-8");
        AlertDialog.Builder builder = getAlertBuilder(context);
        builder.setTitle("Help")
                .setView(webView)
                .show();
    }

    public static AlertDialog.Builder getAlertBuilder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        else
            return new AlertDialog.Builder(context);
    }
}
