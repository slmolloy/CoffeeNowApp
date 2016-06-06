package coffeenow.com.coffeenowapp.helpers;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import coffeenow.com.coffeenowapp.models.User;

public class ApiHelper {

    private static final String LOG_TAG = ApiHelper.class.getSimpleName();

    public static String callApi(String path, String method, User user, JSONObject data) {
        String jsonResponse = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        DataOutputStream output = null;

        try {
            Uri buildUri = Uri.parse(path).buildUpon().build();
            URL url = new URL(buildUri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("accept-language", "en-US,en;q=0.8");
            if (user != null && !TextUtils.isEmpty(user.getId()) && ! TextUtils.isEmpty(user.getAuthToken())) {
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("cache-control", "no-cache");
                connection.setRequestProperty("X-User-Id", user.getId().toString());
                connection.setRequestProperty("X-Auth-Token", user.getAuthToken().toString());
            }
            if (data != null) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/json");
            } else {
                connection.setDoInput(true);
                connection.setDoOutput(false);
            }
            connection.connect();

            if (data != null) {
                output = new DataOutputStream(connection.getOutputStream());
                output.writeBytes(data.toString());
                output.flush();
                output.close();
            }

            int httpResult = connection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                if (stream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                jsonResponse = buffer.toString();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing reader", e);
                }
            }
        }

        return jsonResponse;
    }
}
