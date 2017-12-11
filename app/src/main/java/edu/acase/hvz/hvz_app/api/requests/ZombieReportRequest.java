package edu.acase.hvz.hvz_app.api.requests;

import com.google.gson.JsonParser;

import java.util.List;

import edu.acase.hvz.hvz_app.api.deserializers.ZombieReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.serializers.ZombieReportSerializer;

public final class ZombieReportRequest extends BaseReportRequest<ZombieReportModel> {
    private static final String ENDPOINT_STRING = "http://35.163.170.184/api/v1/zombie_reports";
    private static final String LOG_TAG = "ZombieReportRequest";
    private static final ZombieReportSerializer serializer = new ZombieReportSerializer();
    private static final ZombieReportDeserializer deserializer = new ZombieReportDeserializer();

    public ZombieReportRequest() {
        super(LOG_TAG, ENDPOINT_STRING, serializer, deserializer);
    }

    @Override
    public List<ZombieReportModel> getAll() {
        String response = getResponse(ENDPOINT_STRING);
        return deserializer.deserializeAll(new JsonParser().parse(response), ZombieReportModel.SERIALIZATION.ARRAY_KEY);
    }
}
