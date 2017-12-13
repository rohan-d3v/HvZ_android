package edu.acase.hvz.hvz_app.api.requests;

import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.acase.hvz.hvz_app.Logger;
import edu.acase.hvz.hvz_app.api.deserializers.BaseReportDeserializer;
import edu.acase.hvz.hvz_app.api.models.BaseReportModel;
import edu.acase.hvz.hvz_app.api.serializers.BaseReportSerializer;

/** ReportRequests are used to facilitate transactions with the server. GET, POST, PUT, etc.
 * This is the base used for all specific requests.
 * @param <ReportModel> Which type of ReportModel this should use
 */

public abstract class BaseReportRequest<ReportModel extends BaseReportModel> {
    protected final String LOG_TAG;
    protected static Logger logger = new Logger("ReportRequest");
    protected final String ENDPOINT_STRING;
    protected BaseReportSerializer serializer;
    protected BaseReportDeserializer deserializer;

    protected BaseReportRequest(String LOG_TAG, String ENDPOINT_STRING, BaseReportSerializer serializer, BaseReportDeserializer deserializer) {
        this.LOG_TAG = LOG_TAG;
        this.ENDPOINT_STRING = ENDPOINT_STRING;
        logger = new Logger(LOG_TAG);
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    //public methods

    /** Get a list of all the reports from the server
     * @return all the reports
     */
    public abstract List<ReportModel> getAll();

    /** Create a report on the server (makes a database object)
     * @param report the report to store
     * @return the database id of this report. You need to set this in the report model to use methods like update or delete.
     */
    public int create(ReportModel report) {
        String response = post(ENDPOINT_STRING, serializer.serialize(report, null, null));
        return deserializeCreationResponse(response);
    }

    /** Update a report on the server (updates the db object)
     * @param report the updated report
     * @return true if successfully updated, false otherwise
     */
    public boolean update(ReportModel report) {
        String response = put(ENDPOINT_STRING, report.getDatabase_id(), serializer.serialize(report, null, null));
        return deserializeSuccessResponse(response);
    }

    /** Delete a report from the server (deletes a db object)
     * @param report the report to delete
     * @return true if successfully deleted, false otherwise
     */
    public boolean delete(BaseReportModel report) {
        return delete(ENDPOINT_STRING, report.getDatabase_id());
    }


    //package methods

    /** Perform a GET request on an endpoint
     * @param endpoint the endpoint to connect to
     * @return the server's response
     */
    protected String getResponse(String endpoint) {
        getResponseTask task = new getResponseTask();
        String response;
        try {
            response = task.execute(endpoint).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "GET ERROR: "+e.toString();
            logger.error(response);
        }
        return response;
    }

    /** Perform a POST request on an endpoint
     * @param endpoint the endpoint to connect to
     * @param json the data to send
     * @return the server's response
     */
    protected String post(String endpoint, JsonElement json) {
        postTask task = new postTask();
        String response;
        try {
            response = task.execute(endpoint, json.toString()).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "POST ERROR: "+e.toString();
            logger.error(response);
        }
        return response;
    }

    /** Perform a PUT request on an endpoint
     * @param endpoint the endpoint to connect to
     * @param json the data to send
     * @param database_id the id of the object in the database to PUT to
     * @return the server's response
     */
    protected String put(String endpoint, int database_id, JsonElement json) {
        putTask task = new putTask();
        String response;
        try {
            response = task.execute(endpoint+"/"+database_id, json.toString()).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "PUT ERROR: "+e.toString();
            logger.error(response);
        }
        return response;
    }

    /** Perform a DELETE request on an endpoint
     * @param endpoint the endpoint to connect to
     * @param database_id the database id of the object to delete
     * @return true if the operation succeeds, false otherwise
     */
    protected boolean delete(String endpoint, int database_id) {
        deleteTask task = new deleteTask();
        String response;
        try {
            response = task.execute(endpoint+"/"+database_id).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("DELETE ERROR: "+e.toString());
            return false;
        }
        return response != null;
    }

    /** Deserialize the response from a creation (POST) request
     * @param response the server's response
     * @return the database id of the created object (or -1 if it failed)
     */
    public int deserializeCreationResponse(String response) {
        if (response == null)
            return -1;
        JsonElement element = new JsonParser().parse(response);
        if (element == null || element.isJsonNull())
            return -1;
        return element.getAsJsonObject().get("database_id").getAsInt();
    }

    /** Deserialize the success response from a request
     * @param response the server's response
     * @return true if the request was a success, false otherwise
     */
    public boolean deserializeSuccessResponse(String response) {
        if (response == null)
            return false;
        JsonElement element = new JsonParser().parse(response);
        if (element == null || element.isJsonNull())
            return false;
        return element.getAsJsonObject().get("success").getAsBoolean();
    }


    //http tasks
    /** The task to perform GET requests */
    private static final class getResponseTask extends generalTask {
        @Override
        protected HttpURLConnection setupConnection(String... params) throws IOException {
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
            return connection;
        }
    }

    /** The task to perform POST requests */
    private static final class postTask extends generalTask {
        @Override
        protected HttpURLConnection setupConnection(String... params) throws IOException {
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
            return connection;
        }
    }

    /** The task to perform PUT requests. */
    private static final class putTask extends generalTask {
        @Override
        protected HttpURLConnection setupConnection(String... params) throws IOException {
            String endpoint = params[0];
            String jsonString = params[1];
            logger.debug("PUT \"",endpoint,"\": ",jsonString);
            //setup connection
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestMethod("PUT");
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
            return connection;
        }
    }

    /** The task to perform DELETE requests */
    private static final class deleteTask extends generalTask {
        @Override
        protected HttpURLConnection setupConnection(String... params) throws IOException {
            String endpoint = params[0];
            logger.debug("DELETE \"",endpoint);
            //setup connection
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoInput(true);
            return connection;
        }
    }


    /** The base task to perform requests. Tasks must override setupConnection(...) which will
     * specify the connection type (GET, PUT, POST, etc) and any other details that need
     * to be specified. */
    abstract private static class generalTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        private long startTime;
        protected final int CONNECT_TIMEOUT = 5000;
        protected final int READ_TIMEOUT = 5000;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection connection = setupConnection(params);
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
                throw new Exception(connection.getResponseMessage());
            } catch (Exception e) {
                this.exception = e;
            }
            return null;
        }

        abstract protected HttpURLConnection setupConnection(String... params) throws IOException;

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
