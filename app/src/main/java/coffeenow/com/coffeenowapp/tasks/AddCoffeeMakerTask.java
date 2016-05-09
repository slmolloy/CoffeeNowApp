package coffeenow.com.coffeenowapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import coffeenow.com.coffeenowapp.models.CoffeeMaker;

import static coffeenow.com.coffeenowapp.models.CoffeeMaker.*;
import static coffeenow.com.coffeenowapp.api.CoffeeNow.*;

public class AddCoffeeMakerTask extends AsyncTask<CoffeeMaker, Void, CoffeeMaker> {

    private final String LOG_TAG = AddCoffeeMakerTask.class.getSimpleName();
    private final Context mContext;
    private final CoffeeMaker mCoffeeMaker;

    public AddCoffeeMakerTask(Context context, CoffeeMaker coffeeMaker) {
        mContext = context;
        mCoffeeMaker = coffeeMaker;
    }

    private JSONObject getCoffeeMakerJsonFromObject(CoffeeMaker cm)
            throws JSONException {

        JSONObject result = new JSONObject();
        result.put(CM_ID, cm.getId());
        result.put(CM_NAME, cm.getName());
        result.put(CM_LOCATION, cm.getLocation());
        result.put(CM_VOLUME, cm.getVolume());
        result.put(CM_PRIVATE, cm.isPrivate());
        result.put(CM_LAT, cm.getLatitude());
        result.put(CM_LONG, cm.getLongitude());

        return result;
    }

    private CoffeeMaker getCoffeeMakerObjectFromJson(String jsonStr)
            throws JSONException {

        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        JSONObject jo = new JSONObject(jsonStr);
        CoffeeMaker result = new CoffeeMaker(jo.getString(CM_NAME));
        result.setId(jo.getString(CM_ID));
        result.setToken(jo.getString(CM_TOKEN));
        result.setLocation(jo.getString(CM_LOCATION));
        result.setVolume(jo.getInt(CM_VOLUME));
        result.setPrivate(jo.getBoolean(CM_PRIVATE));
        result.setOn(jo.getBoolean(CM_ON));
        result.setCurrentVolume(jo.getInt(CM_CURRENT_VOLUME));
        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(jo.getString(CM_CREATED));
        result.setCreatedAt(dt.toDate());
        result.setLatitude(jo.getDouble(CM_LAT));
        result.setLongitude(jo.getDouble(CM_LONG));

        return result;
    }

    @Override
    protected CoffeeMaker doInBackground(CoffeeMaker... params) {
        final String COFFEE_MAKER_BASE_URL = API_BASE_URL + API_COFFEE_MAKERS;

        String jsonResponse = null;
        JSONObject jsonObject = null;

        HttpURLConnection connection = null;
        DataOutputStream output = null;
        BufferedReader reader = null;

        try {
            jsonObject = getCoffeeMakerJsonFromObject(params[0]);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error", e);
            e.printStackTrace();
            return null;
        }

        try {
            Uri buildUri = Uri.parse(COFFEE_MAKER_BASE_URL).buildUpon().build();
            URL url = new URL(buildUri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(jsonObject.toString());
            output.flush();
            output.close();

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
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

            return null;
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

        try {
            return getCoffeeMakerObjectFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(CoffeeMaker result) {
        if (result != null && mCoffeeMaker != null) {
            mCoffeeMaker.setId(result.getId());
            mCoffeeMaker.setToken(result.getToken());
            mCoffeeMaker.setCreatedAt(result.getCreatedAt());
            mCoffeeMaker.setCurrentVolume(result.getCurrentVolume());
        }
    }
}
