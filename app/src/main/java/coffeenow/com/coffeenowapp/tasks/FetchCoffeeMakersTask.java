package coffeenow.com.coffeenowapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchCoffeeMakersTask extends AsyncTask<String, Void, String[]> {

    private final String TAG = FetchCoffeeMakersTask.class.getSimpleName();

    private ArrayAdapter<String> mCoffeeMakerAdapter;
    private final Context mContext;

    public FetchCoffeeMakersTask(Context context, ArrayAdapter<String> coffeeMakerAdapter) {
        mContext = context;
        mCoffeeMakerAdapter = coffeeMakerAdapter;
    }

    private String[] getCoffeeMakerDataFromJson(String jsonStr)
            throws JSONException {

        final String CM_ID = "_id";
        final String CM_NAME = "name";
        final String CM_TOKEN = "token";
        final String CM_LOCATION = "location";
        final String CM_VOLUME = "volume";
        final String CM_PRIVATE = "isPrivate";
        final String CM_ON = "on";
        final String CM_CURRENT_VOLUME = "currentVolume";
        final String CM_CREATED = "createdAt";

        JSONArray jsonArray = new JSONArray(jsonStr);
        String[] result = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject coffeeMaker = jsonArray.getJSONObject(i);
            result[i] = coffeeMaker.getString(CM_NAME);
        }
        return result;
    }

    @Override
    protected String[] doInBackground(String... params) {
        //final String COFFEE_MAKER_BASE_URL = "https://tranquil-lowlands-46896.herokuapp.com/api/v1/makers";
        final String COFFEE_MAKER_BASE_URL = "http://10.0.2.2:3000/api/v1/makers";

        String jsonResponse;

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            Uri buildUri = Uri.parse(COFFEE_MAKER_BASE_URL).buildUpon().build();
            URL url = new URL(buildUri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

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
        } catch (IOException e) {
            Log.e(TAG, "Error", e);

            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
        }

        try {
            return getCoffeeMakerDataFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e(TAG, "Error", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null && mCoffeeMakerAdapter != null) {
            mCoffeeMakerAdapter.clear();
            for(String coffeeMaker : result) {
                mCoffeeMakerAdapter.add(coffeeMaker);
            }


        }
    }
}