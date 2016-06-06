package coffeenow.com.coffeenowapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.helpers.ApiHelper;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;
import coffeenow.com.coffeenowapp.models.User;

import static coffeenow.com.coffeenowapp.models.CoffeeMaker.*;
import static coffeenow.com.coffeenowapp.api.CoffeeNow.*;

public class FetchCoffeeMakersTask extends AsyncTask<String, Void, CoffeeMaker[]> {

    private static final String LOG_TAG = FetchCoffeeMakersTask.class.getSimpleName();

    private ArrayAdapter<CoffeeMaker> mCoffeeMakerAdapter;
    private final Context mContext;
    private boolean mMineOnly;

    public FetchCoffeeMakersTask(Context context, ArrayAdapter<CoffeeMaker> coffeeMakerAdapter, boolean showAll) {
        mContext = context;
        mCoffeeMakerAdapter = coffeeMakerAdapter;
        mMineOnly = !showAll;
    }

    private CoffeeMaker[] getCoffeeMakerDataFromJson(String jsonStr)
            throws JSONException {

        if (TextUtils.isEmpty(jsonStr)) {
            Log.e(LOG_TAG, "No json value provided");
            return null;
        }

        JSONArray jsonArray = new JSONArray(jsonStr);
        CoffeeMaker[] result = new CoffeeMaker[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject cm = jsonArray.getJSONObject(i);
            result[i] = new CoffeeMaker(cm.getString(CM_NAME));
            result[i].setId(cm.getString(CM_ID));
            result[i].setToken(cm.getString(CM_TOKEN));
            result[i].setLocation(cm.getString(CM_LOCATION));
            result[i].setVolume(cm.getInt(CM_VOLUME));
            result[i].setPrivate(cm.getBoolean(CM_PRIVATE));
            result[i].setOn(cm.getBoolean(CM_ON));
            result[i].setCurrentVolume(cm.getInt(CM_CURRENT_VOLUME));
            DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(cm.getString(CM_CREATED));
            result[i].setCreatedAt(dt.toDate());
            if (cm.has(CM_LAT) && cm.has(CM_LONG) && !cm.isNull(CM_LAT) && !cm.isNull(CM_LONG)) {
                result[i].setLatitude(cm.getDouble(CM_LAT));
                result[i].setLongitude(cm.getDouble(CM_LONG));
            }
            result[i].setOwner(cm.getString(CM_OWNER));
            result[i].setUsername(cm.getString(CM_USERNAME));
        }
        return result;
    }

    @Override
    protected CoffeeMaker[] doInBackground(String... params) {
        String queryParam = "?mine=" + (mMineOnly ? "true" : "false");
        final String COFFEE_MAKER_BASE_URL = API_BASE_URL + API_COFFEE_MAKERS + queryParam;
        User user = ((CoffeeNowApp) mContext.getApplicationContext()).getUser();
        String jsonResponse;

        jsonResponse = ApiHelper.callApi(COFFEE_MAKER_BASE_URL, "GET", user, null);

        try {
            return getCoffeeMakerDataFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(CoffeeMaker[] result) {
        if (result != null && mCoffeeMakerAdapter != null) {
            mCoffeeMakerAdapter.clear();
            for(CoffeeMaker coffeeMaker : result) {
                mCoffeeMakerAdapter.add(coffeeMaker);
            }
        }
    }
}