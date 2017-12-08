package edu.acase.hvz.hvz_app.api.requests;

import com.google.gson.JsonParser;

import java.util.List;

import edu.acase.hvz.hvz_app.api.deserializers.HumanReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.serializers.HumanReportSerializer;

public final class HumanReportRequest extends BaseReportRequest<HumanReportModel> {
    private static final String ENDPOINT_STRING = "http://35.163.170.184/api/v1/human_reports";
    private static final String LOG_TAG = "HumanReportRequest";
    private static final HumanReportSerializer serializer = new HumanReportSerializer();
    private static final HumanReportDeserializer deserializer = new HumanReportDeserializer();


    public HumanReportRequest() {
        super(LOG_TAG);
    }

    @Override
    public List<HumanReportModel> getAll() {
        String response = getResponse(ENDPOINT_STRING);
        return deserializer.deserializeAll(new JsonParser().parse(response), null, null);
    }

    @Override
    public int create(HumanReportModel report) {
        String response = post(ENDPOINT_STRING, serializer.serialize(report, null, null));
        return deserializeCreationResponse(response);
    }

    @Override
    public boolean delete(HumanReportModel report) {
        return delete(ENDPOINT_STRING, report.getDatabase_id());
    }

    @Override
    public boolean update(HumanReportModel report) {
        String response = put(ENDPOINT_STRING, report.getDatabase_id(), serializer.serialize(report, null, null));
        return deserializeSuccessResponse(response);
    }
}
