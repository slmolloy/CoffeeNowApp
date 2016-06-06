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

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.helpers.ApiHelper;
import coffeenow.com.coffeenowapp.models.User;

import static coffeenow.com.coffeenowapp.models.User.*;
import static coffeenow.com.coffeenowapp.api.CoffeeNow.*;

public class FetchUsersTask extends AsyncTask<String, Void, User[]> {

    private static final String LOG_TAG = FetchUsersTask.class.getSimpleName();
    private ArrayAdapter<User> mUsersAdapter;
    private final Context mContext;

    public FetchUsersTask(Context context, ArrayAdapter<User> usersAdapter) {
        mContext = context;
        mUsersAdapter = usersAdapter;
    }

    private User[] getUsersDataFromJson(String jsonStr)
            throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonStr);
        User[] result = new User[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            result[i] = new User(
                    user.getString(USER_ID),
                    user.getString(USER_NAME));
        }

        return result;
    }

    protected User[] doInBackground(String... params) {
        final String USERS_BASE_URL = API_BASE_URL + API_USERS;

        String jsonResponse = "";

        User user = ((CoffeeNowApp) mContext.getApplicationContext()).getUser();

        jsonResponse = ApiHelper.callApi(USERS_BASE_URL, "GET", user, null);

        try {
            return getUsersDataFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User[] result) {
        if (result != null && mUsersAdapter != null) {
            mUsersAdapter.clear();
            for (User user : result) {
                mUsersAdapter.add(user);
            }
        }
    }
}
