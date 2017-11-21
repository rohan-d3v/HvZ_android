package edu.acase.hvz.hvz_app.api.requests;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;

public final class ZombieReportRequest {
    private static String ENDPOINT_STRING = "http://35.163.170.184/api/v1/zombie_reports";

    private static void debug(String... debugMsgs) {
        for (String debugMsg: debugMsgs)
            Log.d("ZombieReportRequest", debugMsg);
    }

    public List<ZombieReportModel> fetchAll() {
        debug("fetch all zombie reports");
        String response = getResponse();
        debug("got response: \'", response, "\'");
        /*CustomJsonDeserializer deserializer = new CustomJsonDeserializer();
        List<ZombieReportModel> zombieReportModels = deserializer.deserializeAll(new JsonParser().parse(response), null, null);
        debug("zombie reports: [");
        for(ZombieReportModel zombieReport: zombieReportModels){
            debug(zombieReport.toString());
        }
        debug("]");
        return zombieReportModels;*/
        return null;
    }

    private String getResponse() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ENDPOINT_STRING).openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append('\n');
                    }
                    br.close();
                    return sb.toString();
            }
            connection.disconnect();
        } catch (Exception e) {
            return "ERROR - exception: "+e.toString();
        }
        return "ERROR";
    }

    public void editReport(int reportId) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(ENDPOINT_STRING).openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class CustomJsonDeserializer implements JsonDeserializer<ZombieReportModel> {
        public List<ZombieReportModel> deserializeAll(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            List<ZombieReportModel> zombieReportModels = new ArrayList<>();
            JsonArray jsonArray = (JsonArray) json;

            try {
                for(JsonElement jsonElement: jsonArray) {
                    ZombieReportModel report = deserialize(jsonElement, null, null);
                    if (report != null)
                        zombieReportModels.add(report);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return zombieReportModels;
        }
        @Override
        public ZombieReportModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            ZombieReportModel zombieReportModel = null;
            JsonObject object = json.getAsJsonObject();

            try {
                zombieReportModel = new ZombieReportModel(object.get("id").getAsInt());
                zombieReportModel.setNumZombies(object.get("num_zombies").getAsInt());
                zombieReportModel.setTimeSighted(new Date(object.get("time_sighted").getAsString()));
                zombieReportModel.setLocation(new LatLng(object.get("location_lat").getAsDouble(), object.get("location_long").getAsDouble()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return zombieReportModel;
        }
    }
}
