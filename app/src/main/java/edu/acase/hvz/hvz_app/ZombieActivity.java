package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.HumanReportRequest;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;
import edu.acase.hvz.hvz_app.reports.CreateHumanReportActivity;
import edu.acase.hvz.hvz_app.reports.EditHumanReportActivity;

public class ZombieActivity extends PlayerActivity<HumanReportModel> {
    private static final Logger logger = new Logger("zombie_activity");
    private static final HumanReportRequest humanReportRequest = new HumanReportRequest();

    public ZombieActivity() {
        super(logger, humanReportRequest);
    }

    @Override
    int getContentView() {
        return R.layout.activity_zombie;
    }

    @Override
    Class<?> getCaughtButtonIntentClass() {
        return StunnedActivity.class;
    }

    @Override
    Class<?> getCreateReportIntentClass() {
        return CreateHumanReportActivity.class;
    }

    @Override
    Class<?> getEditReportIntentClass() {
        return EditHumanReportActivity.class;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        addHeatMap();
    }

    private void addHeatMap() {
        ZombieReportRequest zombieReportRequest = new ZombieReportRequest();
        List<ZombieReportModel> hReport = zombieReportRequest.getAll();
        ArrayList<LatLng> pos  = new ArrayList<LatLng>();
        for (int i = 0; i < hReport.size(); i++) {
            pos.add(hReport.get(i).getLoc());
        }
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                .05f, .2f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().gradient(gradient).radius(50).data(pos).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        gmap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Button infoButton = (Button) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(view -> {
            Intent i = new Intent(getBaseContext(), HumanActivity.class);
            startActivity(i);
            finish(); //prevent back button
        });
    }
}
