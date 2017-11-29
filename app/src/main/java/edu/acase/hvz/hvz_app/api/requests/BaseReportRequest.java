package edu.acase.hvz.hvz_app.api.requests;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.acase.hvz.hvz_app.Logger;

public abstract class BaseReportRequest<ReportModel> {
    protected final String LOG_TAG;
    protected final String ENDPOINT_STRING;
    protected final Logger logger;

    BaseReportRequest(String LOG_TAG, String ENDPOINT_STRING) {
        this.ENDPOINT_STRING = ENDPOINT_STRING;
        this.LOG_TAG = LOG_TAG;
        logger = new Logger(LOG_TAG);
    }

    public abstract List<ReportModel> fetchAll();

    protected String getResponse() {
        logger.debug(false, "GET \"",ENDPOINT_STRING,"\"");
        getResponseTask task = new getResponseTask();
        String response;
        try {
            response = task.execute(ENDPOINT_STRING).get(task.CONNECT_TIMEOUT + task.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            response = "ERROR: "+e.toString();
        }
        logger.debug(false, "response: \"", response, "\"");
        return response;
    }

    protected class getResponseTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        private long startTime;
        public final int CONNECT_TIMEOUT = 5000;
        public final int READ_TIMEOUT = 5000;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String endpoint = params[0];
                HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
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
            logger.debug(false, String.format(Locale.US, "[%d ms] response: \'%s\'", executionTime, response));
            if (exception != null)
                logger.error(exception.toString());
        }

        public Exception getException() { return exception; }
    }
}
