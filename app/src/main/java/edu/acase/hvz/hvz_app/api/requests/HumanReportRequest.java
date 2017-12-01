package edu.acase.hvz.hvz_app.api.requests;

import com.google.gson.JsonElement;
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
import edu.acase.hvz.hvz_app.api.serializers.HumanReportSerializer;

public final class HumanReportRequest extends BaseReportRequest<HumanReportModel> {
    private static final String ENDPOINT_STRING = "http://35.163.170.184/api/v1/human_reports";
    private static final String LOG_TAG = "HumanReportRequest";

    public HumanReportRequest() {
        super(LOG_TAG);
    }

    @Override
    public List<HumanReportModel> getAll() {
        String response = getResponse(ENDPOINT_STRING);
        HumanReportReportDeserializer deserializer = new HumanReportReportDeserializer();
        List<HumanReportModel> humanReportModels = deserializer.deserializeAll(new JsonParser().parse(response), null, null);
        return humanReportModels;
    }

    @Override
    public String post(HumanReportModel report) {
        HumanReportSerializer serializer = new HumanReportSerializer();
        JsonElement reportJson = serializer.serialize(report, null, null);
        String response = postTo(ENDPOINT_STRING, reportJson);

        logger.debug(false, "model: ", report.toString());
        logger.debug(false,"json: ",reportJson.toString());

        return response;
    }

    @Override
    public String delete(HumanReportModel report) {
        return delete(ENDPOINT_STRING, report.getDATABASE_ID());
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
