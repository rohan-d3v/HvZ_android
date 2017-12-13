package edu.acase.hvz.hvz_app.api.requests;

import com.google.gson.JsonParser;

import java.util.List;

import edu.acase.hvz.hvz_app.api.deserializers.HumanReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.HumanReportModel;
import edu.acase.hvz.hvz_app.api.serializers.HumanReportSerializer;

/** Make server requests for human reports
 * @see BaseReportRequest the base report request */

public final class HumanReportRequest extends BaseReportRequest<HumanReportModel> {
    private static final String ENDPOINT_STRING = "http://35.163.170.184/api/v1/human_reports";
    private static final String LOG_TAG = "HumanReportRequest";
    private static final HumanReportSerializer serializer = new HumanReportSerializer();
    private static final HumanReportDeserializer deserializer = new HumanReportDeserializer();

    public HumanReportRequest() {
        super(LOG_TAG, ENDPOINT_STRING, serializer, deserializer);
    }

    @Override
    public List<HumanReportModel> getAll() {
        String response = getResponse(ENDPOINT_STRING);
        return deserializer.deserializeAll(new JsonParser().parse(response), HumanReportModel.SERIALIZATION.ARRAY_KEY);
    }
}
