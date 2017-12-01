package edu.acase.hvz.hvz_app.api.requests;

import android.os.AsyncTask;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.api.models.BaseReportModel;

public abstract class BaseReportRequest<ReportModel extends BaseReportModel> {
    protected final String LOG_TAG;
    protected static Logger logger = new Logger("ReportRequest");

    protected BaseReportRequest(String LOG_TAG) {
        this.LOG_TAG = LOG_TAG;
        logger = new Logger(LOG_TAG);
    }


    //public methods

    public abstract List<ReportModel> getAll();
    public abstract int create(ReportModel report);
    public abstract boolean delete(ReportModel report);

    //package methods

    protected String getResponse(String endpoint) {
        getResponseTask task = new getResponseTask();
        String response;
        try {
            response = task.execute(endpoint).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "ERROR: "+e.toString();
        }
        return response;
    }

    protected String postTo(String endpoint, JsonElement json) {
        postTask task = new postTask();
        String response;
        try {
            response = task.execute(endpoint, json.toString()).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "ERROR: "+e.toString();
        }
        return response;
    }

    protected boolean delete(String endpoint, int database_id) {
        deleteTask task = new deleteTask();
        String response;
        try {
            JsonObject deleteJson = new JsonObject();
            deleteJson.addProperty("id", database_id);
            response = task.execute(endpoint, deleteJson.toString()).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "ERROR: "+e.toString();
        }
        if (!response.contains("Not Found")) return true;
        else return false;
    }

    public int deserializeCreationResponse(String response) {
        CreationResponseDeserializer deserializer = new CreationResponseDeserializer();
        CreationResponse creationResponse = deserializer.deserialize(new JsonParser().parse(response), null, null);
        if (creationResponse == null) return -1;
        else return creationResponse.DATABASE_ID;
    }


    //private methods & classes

    private final class CreationResponseDeserializer implements JsonDeserializer<CreationResponse> {
        @Override
        public CreationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            CreationResponse response = null;
            JsonObject object = json.getAsJsonObject();
            try {
                response = new CreationResponse(object.get(CreationResponse.DATABASE_ID_SERIALIZATION).getAsInt());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private final class CreationResponse {
        public static final String DATABASE_ID_SERIALIZATION = "database_id";
        public final int DATABASE_ID;

        public CreationResponse(int DATABASE_ID) {
            this.DATABASE_ID = DATABASE_ID;
        }
    }

    private static final class getResponseTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        private long startTime;
        private final int CONNECT_TIMEOUT = 5000;
        private final int READ_TIMEOUT = 5000;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String endpoint = params[0];
                logger.debug("GET \"",endpoint,"\"");
                //setup connection
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setDoInput(true);
                //parse response
                int status = connection.getResponseCode();
                switch (status) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_CREATED:
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
                this.exception = e;
            } finally {
                //connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug(String.format(Locale.US, "[%d ms] response: \n%s", executionTime, response));
            if (exception != null)
                logger.error(exception.toString());
        }

        public Exception getException() { return exception; }
    }

    private static final class postTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        private long startTime;
        private final int CONNECT_TIMEOUT = 5000;
        private final int READ_TIMEOUT = 5000;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String endpoint = params[0];
                String jsonString = params[1];
                logger.debug("POST \"",endpoint,"\": ",jsonString);
                //setup connection
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                //send json
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonString.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //parse response
                int status = connection.getResponseCode();
                switch (status) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_CREATED:
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
                return connection.getResponseMessage();
            } catch (Exception e) {
                this.exception = e;
            } finally {
                //connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug(String.format(Locale.US, "[%d ms] response: \n%s", executionTime, response));
            if (exception != null)
                logger.error(exception.toString());
        }

        public Exception getException() { return exception; }
    }

    private static final class deleteTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        private long startTime;
        private final int CONNECT_TIMEOUT = 5000;
        private final int READ_TIMEOUT = 5000;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String endpoint = params[0];
                String jsonString = params[1];
                logger.debug("DELETE \"",endpoint,"\": ",jsonString);
                //setup connection
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                //send json
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonString.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //parse response
                int status = connection.getResponseCode();
                switch (status) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_CREATED:
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
                return connection.getResponseMessage();
            } catch (Exception e) {
                this.exception = e;
            } finally {
                //connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug(String.format(Locale.US, "[%d ms] response: \n%s", executionTime, response));
            if (exception != null)
                logger.error(exception.toString());
        }

        public Exception getException() { return exception; }
    }
}
