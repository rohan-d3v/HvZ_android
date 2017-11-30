package edu.acase.hvz.hvz_app.api.requests;

import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.acase.hvz.hvz_app.api.deserializers.HumanReportReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;

public final class HumanReportRequest extends BaseReportRequest<HumanReportModel> {
    private static final String ENDPOINT_STRING = "http://35.163.170.184/api/v1/human_reports";
    private static final String LOG_TAG = "HumanReportRequest";

    public HumanReportRequest() {
        super(LOG_TAG, ENDPOINT_STRING);
    }

    @Override
    public List<HumanReportModel> fetchAll() {
        String response = getResponse();
        HumanReportReportDeserializer deserializer = new HumanReportReportDeserializer();
        List<HumanReportModel> humanReportModels = deserializer.deserializeAll(new JsonParser().parse(response), null, null);

        logger.debug("human reports: [");
        for(HumanReportModel zombieReport: humanReportModels){
            logger.debug(zombieReport.toString());
        }
        logger.debug("]");

        return humanReportModels;
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
}
