package coffeenow.com.coffeenowapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.helpers.ApiHelper;
import coffeenow.com.coffeenowapp.models.User;

import static coffeenow.com.coffeenowapp.api.CoffeeNow.*;

public class RemoveCoffeeMakerTask extends AsyncTask<String, Void, Boolean> {

    private static final String LOG_TAG = RemoveCoffeeMakerTask.class.getSimpleName();
    private final Context mContext;

    public RemoveCoffeeMakerTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length != 1 || TextUtils.isEmpty(params[0])) {
            return false;
        }

        final String COFFEE_MAKER_DELETE_URL = API_BASE_URL + API_COFFEE_MAKERS + '/' + params[0];

        User user = ((CoffeeNowApp) mContext.getApplicationContext()).getUser();

        String jsonReponse = ApiHelper.callApi(COFFEE_MAKER_DELETE_URL, "DELETE", user, null);

        Log.v(LOG_TAG, "Response: " + jsonReponse);

        return true;
    }
}
