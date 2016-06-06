package coffeenow.com.coffeenowapp.tasks;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.helpers.ApiHelper;
import coffeenow.com.coffeenowapp.models.User;

import static coffeenow.com.coffeenowapp.models.User.*;
import static coffeenow.com.coffeenowapp.api.CoffeeNow.*;

public class LoginTask extends AsyncTask<String, Void, User> {

    private static final String LOG_TAG = LoginTask.class.getSimpleName();
    private final OnTaskComplete mListener;
    private final User mUser;

    public LoginTask(OnTaskComplete listener, User user) {
        mListener = listener;
        mUser = user;
    }

    private JSONObject getCredentialsJson(String username, String password)
            throws JSONException {
        JSONObject result = new JSONObject();
        result.put(USER_NAME, username);
        result.put(USER_PASSWORD, password);
        return result;
    }

    private void getAuthDataFromJson(String jsonStr)
            throws JSONException {
        JSONObject responseObject = new JSONObject(jsonStr);
        JSONObject dataObject = responseObject.getJSONObject(USER_AUTH_DATA);
        mUser.setId(dataObject.getString(USER_AUTH_ID));
        mUser.setAuthToken(dataObject.getString(USER_AUTH_TOKEN));
    }

    @Override
    protected User doInBackground(String... params) {
        final String LOGIN_BASE_URL = API_BASE_URL + API_LOGIN;

        if (params.length != 2) {
            return null;
        }

        String jsonResponse;
        JSONObject credentials;

        try {
            credentials = getCredentialsJson(params[0], params[1]);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to create credentials data");
            return null;
        }

        jsonResponse = ApiHelper.callApi(LOGIN_BASE_URL, "POST", null, credentials);

        try {
            getAuthDataFromJson(jsonResponse);
            mUser.setUsername(params[0]);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to get auth");
        }

        return mUser;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user == null || TextUtils.isEmpty(user.getId()) || TextUtils.isEmpty(user.getAuthToken())) {
            mListener.onLoginComplete(false);
        } else {
            mListener.onLoginComplete(true);
        }
    }

    public interface OnTaskComplete {
        void onLoginComplete(boolean successful);
    }
}
